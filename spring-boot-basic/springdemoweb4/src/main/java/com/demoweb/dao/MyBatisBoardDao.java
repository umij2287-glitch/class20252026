package com.demoweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.demoweb.dto.BoardAttachDto;
import com.demoweb.dto.BoardDto;

@Component("myBatisBoardDao") // @Component 로 등록하는 것은 <bean... 으로 servlet-context.xml 에 등록하는 것과 같음.
public class MyBatisBoardDao implements BoardDao {
	
	private SqlSessionTemplate template;
	public MyBatisBoardDao(SqlSessionTemplate template) {
		this.template = template;
	}
	final String BOARD_MAPPER = "com.demoweb.mapper.BoardMapper";
	
	@Override
	public void insertBoard(BoardDto board) {
		// board.getBoardNo() --> 0
		template.insert(BOARD_MAPPER + ".insertBoard", board);
		// board.getBoardNo() --> 새로 생성된 글 번호.
	}
	
	@Override
	public ArrayList<BoardDto> showBoard() {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardDto> list = new ArrayList<>();
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "select * from tbl_board order by boardno desc";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				BoardDto board = new BoardDto();
				board.setBoardNo(rs.getInt(1));
				board.setWriter(rs.getString(2));
				board.setTitle(rs.getString(3));
				board.setContent(rs.getString(4));
				board.setWriteDate(rs.getDate(5));
				board.setModifyDate(rs.getDate(6));
				board.setReadCount(rs.getInt(7));
				board.setCategory(rs.getString(8));
				list.add(board);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return list;
	}

	@Override
	public ArrayList<BoardDto> showBoardByPage(int start, int count) {
		
		HashMap<String, Integer> params = new HashMap<>();
		params.put("start", start);
		params.put("count", count);
		
		List<BoardDto> list = template.selectList(BOARD_MAPPER + ".showBoardByPage", params);
		return new ArrayList<BoardDto>(list);
	}
	
	@Override
	public BoardDto selectBoardByBoardNo(int boardNo) {
		
//		BoardDto board = template.selectOne(BOARD_MAPPER + ".selectBoardByBoardNo", boardNo);
//		
//		return board;
		
		return template.selectOne(BOARD_MAPPER + ".selectBoardByBoardNoWithResultMap", boardNo);
		
	}

	@Override
	public void updateBoardReadCount(int boardNo) {
		template.update(BOARD_MAPPER + ".updateBoardReadCount", boardNo);
	}


	@Override
	public void deleteBoardByNo(int boardNo) {
			template.update(BOARD_MAPPER + ".deleteBoardByNo", boardNo);
	}

	@Override
	public int selectBoardCount() {
		return template.selectOne(BOARD_MAPPER + ".selectBoardCount");
	}

	@Override
	public void insertBoardAttach(BoardAttachDto attach) {
		
		template.insert(BOARD_MAPPER + ".insertBoardAttach", attach);

	}

	@Override
	public ArrayList<BoardAttachDto> selectBoardAttachmentsByBoardNo(int boardNo) {
		
		List<BoardAttachDto> attachments = template.selectList(BOARD_MAPPER + ".selectBoardAttachmentsByBoardNo", boardNo);
		return new ArrayList<BoardAttachDto>(attachments);

	}

	@Override
	public BoardAttachDto selectBoardAttachByAttachNo(int attachNo) {
		
		return template.selectOne(BOARD_MAPPER + ".selectBoardAttachByAttachNo", attachNo);

	}

	@Override
	public void updateBoard(BoardDto board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "update tbl_board set title = ?, content = ? where boardno = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getContent());
			pstmt.setInt(3, board.getBoardNo());
			pstmt.executeUpdate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
	}	
	
	@Override
	public void deleteBoardAttach(int attachNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			//1. 드라이버 준비
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//2. 연결 객체 만들기
			conn = 
			DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", // 서버와 DB 위치 지정 
										"human", "human"); // 아이디, 패스워드
			
			// 3. SQL 작성 + 명령 객체 만들기			
			String sql = 
				"delete from tbl_boardattach where attachno = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attachNo);
			
			// 4. 명령 실행 + 결과가 있다면(select 인 경우) 결과 수신 
			// pstmt.executeQuery(); // select
			pstmt.executeUpdate(); // insert, update, delete, create, alter, drop, ...
			

		} catch (Exception ex) {
			ex.printStackTrace(); // 오류 메시지를 화면에 출력
		} finally {
			// 6. 연결 종료
			try { pstmt.close(); } catch (Exception ex) {}
			try { conn.close(); } catch (Exception ex) {}
		}
	}

	@Override
	public BoardDto selectBoardByBoardNo2(int boardNo) {
		
		// return template.selectOne(BOARD_MAPPER + ".selectBoardByBoardNoWithJoin", boardNo);
		
		return template.selectOne(BOARD_MAPPER + ".selectBoardByBoardNoWithSelect", boardNo);
		
	}
}
