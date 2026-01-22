package com.demoweb.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.demoweb.common.Util;
import com.demoweb.dao.BoardDao;
import com.demoweb.dao.MySqlBoardDao;
import com.demoweb.dto.BoardAttachDto;
import com.demoweb.dto.BoardDto;
import com.demoweb.dto.MemberDto;
import com.demoweb.ui.ThePager;
import com.demoweb.view.DownloadView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

    private final MySqlBoardDao boardDao_1;

    private final AuthController authController;
	// MySqlBoardDao 객체를 IoC 컨테이너에 등록.
	// BoardDao boardDao = new MySqlBoardDao();
	// @Autowired // 의존 객체 자동 주입. -> 변수에 바로 @Autowired 를 쓰는 것은 권장하지 않음.
	// @Qualifier("boardDao") // @Component 로 등록한 이름과 동일해야 함. 
	// private BoardDao boardDao;
	// 권장 방법.
	private BoardDao boardDao;
	
	public BoardController(BoardDao boardDao, AuthController authController, MySqlBoardDao boardDao_1) { // 생성자의 전달인자는 자동으로 의존 객체 주입.
		this.boardDao = boardDao; // 생성자의 전달인자는 자동으로 의존 객체 주입.
		this.authController = authController; // 생성자의 전달인자는 자동으로 의존 객체 주입.
		this.boardDao_1 = boardDao_1;
	}
//	// 전체 조회	
//	@GetMapping(path = {"/board/list"})
//	public String boardView(
//			Model model) { // Model 타입 전달인자는 View 로 데이터를 전달하는 도구.
//		ArrayList<BoardDto> list = boardDao.showBoard();
//		model.addAttribute("list", list); // 실제로는 request 객체에 저장.
//		return "board/list";
//	}
	
	@GetMapping(path = {"/board/list"})
	public String boardView(
			@RequestParam(value="pageNo", defaultValue = "1") int pageNo,
			Model model) { // Model 타입 전달인자는 View 로 데이터를 전달하는 도구.
		int pageSize = 3; // 한 페이지에 보여줄 갯수.
		int pagerSize= 3; // 한번에 표시되는 페이지 번호의 갯수.
		int start = (pageNo - 1) * pageSize;
		int dataCount = boardDao.selectBoardCount();
		// 페이지 번호 이전 다음, 처음, 마지막 등의 링크를 만드는 클래스의 인스턴스 만들기
		ThePager pager = new ThePager(dataCount, pageNo, pageSize, pagerSize,"list", null);
		
		ArrayList<BoardDto> list = boardDao.showBoardByPage(start, pageSize);
		model.addAttribute("list", list); // 실제로는 request 객체에 저장.
		model.addAttribute("pager", pager);
		model.addAttribute("pageNo", pageNo);
		return "board/list";
	}
	
	@GetMapping(path = {"/board/write"})
	public String boardWriteView() {
		return "board/write";
	}
	
//	@PostMapping(path = {"/board/write"})
//	public String boardWrite(BoardDto newBoard, HttpSession session) {
//		boardDao.insertBoard(newBoard);
//		return "redirect:list";
//	}
	
	@PostMapping(path = {"/board/write"})
	public String boardWrite(BoardDto newBoard,
							 @RequestParam("attach") MultipartFile attach, // <input type="file" name="attach" 데이터 수신
							 MultipartHttpServletRequest request) { 
		
//		MultipartFile attach2 = request.getFile("attach");
//		if (attach2 != null) {
//			System.out.println("---------->" + attach2.getOriginalFilename());
//		}
		
		if (attach != null) {
			// System.out.println("---------->" + attach.getOriginalFilename());
			BoardAttachDto attachment = new BoardAttachDto();
			ArrayList<BoardAttachDto> attachments = new ArrayList<>();
			try {
				String dir = request.getServletContext().getRealPath("/board-attach");
				String userFileName = attach.getOriginalFilename();
				String savedFileName = Util.makeUniqueFileName(userFileName);			
				attach.transferTo(new File(dir, savedFileName)); // 파일 저장
				
				attachment.setUserFileName(userFileName);
				attachment.setSavedFileName(savedFileName);
				attachments.add(attachment);			
				newBoard.setAttachments(attachments);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// SQL 에서 boardno 는 insertBoard 이후에 결정됨.(auto_increment)
		// 그 전에 boardno 를 지정할 수 없다.
		System.out.println("--------------->" + newBoard.getBoardNo());
		boardDao.insertBoard(newBoard);
		System.out.println("--------------->" + newBoard.getBoardNo());
		
		if (newBoard.getAttachments() != null || newBoard.getAttachments().size() > 0) {
			for (BoardAttachDto a : newBoard.getAttachments()) {
				a.setBoardNo(newBoard.getBoardNo());
				boardDao.insertBoardAttach(a);
			}
		}
		
		return "redirect:list";
	}
	
	@GetMapping(path = {"/board/detail"})
	public String detail(
			@RequestParam("boardNo") int boardNo,
			@RequestParam("pageNo") int pageNo,
			Model model,
			HttpSession session
			) {
		// 요청 데이터 읽기. (전달인자 직접 수신.)
		// 요청 처리.
		BoardDto board = boardDao.selectBoardByBoardNo(boardNo);
		if ( board == null) {
			return "redirect:list";
		}
		// 첨부파일 조회
		ArrayList<BoardAttachDto> attachments = boardDao.selectBoardAttachmentsByBoardNo(boardNo);
		board.setAttachments(attachments);
		// 요청 처리: 조회수 증가. 데이터베이스 데이터 수정.
		ArrayList<Integer> readList = (ArrayList<Integer>) session.getAttribute("readlist"); // 세션에 저장된 읽은 글 목록 조회
		if (readList == null) {
			readList = new ArrayList<>();
		}
		if(!readList.contains(boardNo)) { // 현재 글 번호가 읽은 글 목록에 없다면,
			boardDao.updateBoardReadCount(boardNo);
			board.setReadCount(board.getReadCount() + 1); // 조회수 증가.
			readList.add(boardNo); // 읽은 글 목록에 현재 글 번호 추가.
		}
		session.setAttribute("readlist", readList);
		
		// view 에서 읽을 수 있도록 데이터 전달. // Model 타입 전달인자에 저장.
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		// view 로 이동.
		return "board/detail";
	}
	
	@GetMapping(path = {"/board/detail/{boardNo}"}) // {boardNo}: 경로의 일부이면서 데이터
	public String detail2(
			@PathVariable("boardNo") int boardNo, // @PathVariable: 경로에 포함된 데이터 읽기
			@RequestParam("pageNo") int pageNo,
			Model model,
			HttpSession session
			) {
		// 요청 데이터 읽기. (전달인자 직접 수신.)
		// 요청 처리.
		BoardDto board = boardDao.selectBoardByBoardNo(boardNo);
		if ( board == null) {
			return "redirect:/board/list";
		}
		// 첨부파일 조회
		ArrayList<BoardAttachDto> attachments = boardDao.selectBoardAttachmentsByBoardNo(boardNo);
		board.setAttachments(attachments);
		// 요청 처리: 조회수 증가. 데이터베이스 데이터 수정.
		ArrayList<Integer> readList = (ArrayList<Integer>) session.getAttribute("readlist"); // 세션에 저장된 읽은 글 목록 조회
		if (readList == null) {
			readList = new ArrayList<>();
		}
		if(!readList.contains(boardNo)) { // 현재 글 번호가 읽은 글 목록에 없다면,
			boardDao.updateBoardReadCount(boardNo);
			board.setReadCount(board.getReadCount() + 1); // 조회수 증가.
			readList.add(boardNo); // 읽은 글 목록에 현재 글 번호 추가.
		}
		session.setAttribute("readlist", readList);
		// view 에서 읽을 수 있도록 데이터 전달. // Model 타입 전달인자에 저장.
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		// view 로 이동.
		return "board/detail";
	}
	
	@GetMapping(path = {"/board/delete/{boardNo}"})
	public String deletePost(
			HttpSession session,
			@PathVariable("boardNo") int boardNo,
			@RequestParam("pageNo") int pageNo
			) {
		
		BoardDto board = boardDao.selectBoardByBoardNo(boardNo);
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		// 사용자가 delete 주소를 직접 입력할 경우 방지
		if (board == null || !member.getMemberId().equals(board.getWriter())) {
			return "redirect:/board/list";
		}
		
		boardDao.deleteBoardByNo(boardNo);
		return "redirect:/board/list?pageNo=" +pageNo;
	}
	
	@GetMapping(path= {"/board/download/{attachNo}"})
	public View download(
						   @PathVariable("attachNo") int attachNo,
						   Model model) {
		// System.out.println("---------->" + attachNo);
		// 요청 데이터를 전달인자를 통해 읽기
		// 요청 처리 - 데이터 조회
		BoardAttachDto attachment = boardDao.selectBoardAttachByAttachNo(attachNo);
		if (attachment == null) {
			// return "redirect:/board/list"; // 오류 반환값이 불일치.
			return new RedirectView("/board/list");
		}
		// view 에서 읽을 수 있도록 Model 타입 전달인자에 데이터 저장.
		model.addAttribute("attachment", attachment);
		// 요청 처리 - 다운로드 처리 -> 응답 컨텐츠는 파일
		// return "download"; // jsp 로 응답 컨텐츠 생산 -> HTML 응답.
		// return "some message" + @ResponseBody // 텍스트 응답.
		return new DownloadView();
	}
	
	@GetMapping(path = {"/board/edit/{boardNo}"})
	public String editForm(
			@PathVariable("boardNo") int boardNo,
			@RequestParam("pageNo") int pageNo,
			Model model
			) {
		BoardDto board = boardDao.selectBoardByBoardNo(boardNo);
		if (board == null) {
			return "redirect: /board/list";
		}
		ArrayList<BoardAttachDto> attachments = boardDao.selectBoardAttachmentsByBoardNo(boardNo);
		board.setAttachments(attachments);
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		return "board/edit";
	}
	
	@PostMapping(path = {"/board/edit"})
	public String edit(BoardDto board,
			@RequestParam("attach") MultipartFile attach,
			@RequestParam("pageNo") int pageNo,
			HttpServletRequest req
			) {
		
		if (attach != null && attach.getOriginalFilename().length() > 0) {
			BoardAttachDto attachment = new BoardAttachDto();
			ArrayList<BoardAttachDto> attachments = new ArrayList<>();
			try {
				String dir = req.getServletContext().getRealPath("/board-attach");
				String userFileName = attach.getOriginalFilename();
				String savedFileName = Util.makeUniqueFileName(userFileName);			
				attach.transferTo(new File(dir, savedFileName)); // 파일 저장
				
				attachment.setBoardNo(board.getBoardNo());
				attachment.setUserFileName(userFileName);
				attachment.setSavedFileName(savedFileName);
				attachments.add(attachment);			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			board.setAttachments(attachments);
		}
		
		try {		
			boardDao.updateBoard(board);
			if (board.getAttachments() != null && board.getAttachments().size() > 0) {
				for (BoardAttachDto a : board.getAttachments()) {
					a.setBoardNo(board.getBoardNo()); // board를 insert한 이후에 글번호 사용 가능
					boardDao.insertBoardAttach(a);
				}
			}	
		} catch (Exception ex) {
			System.out.println("글수정 실패");
			return String.format("redirect:edit/%d?pageNo=%d", board.getBoardNo(), pageNo);
		}
		return String.format("redirect:detail?boardNo=%d&pageNo=%d", board.getBoardNo(), pageNo);
	}
	
	@GetMapping(path = {"/board/delete-attach/{attachNo}"})
	@ResponseBody
	public String deleteAttach(
			@PathVariable("attachNo") int attachNo
			) {
		try {
		boardDao.deleteBoardAttach(attachNo);
		} catch (Exception ex) {
			return "fail";
		}
		
		return "success";
	}
	
}