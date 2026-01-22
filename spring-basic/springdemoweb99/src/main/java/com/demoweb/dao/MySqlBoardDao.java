package com.demoweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.demoweb.dto.BoardAttachDto;
import com.demoweb.dto.BoardDto;

@Component("boardDao") // @Component 로 등록하는 것은 <bean... 으로 servlet-context.xml 에 등록하는 것과 같음.
public class MySqlBoardDao implements BoardDao {
	
	@Override
	public void insertBoard(BoardDto board) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "insert into tbl_board (writer, title, content) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getWriter());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			pstmt.executeUpdate();
			pstmt.close();
		
			sql = "select last_insert_id()"; // 현제 세션에서 마지막으로 생성된 auto_increment 값
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int newBoardNo = rs.getInt(1);
				board.setBoardNo(newBoardNo);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception ex) {}
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
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
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardDto> list = new ArrayList<>();
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "select * from tbl_board order by boardno desc limit ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, count);
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
				board.setDeleted(rs.getBoolean(9));
				list.add(board);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception ex) {}
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return list;
	}
	@Override
	public BoardDto selectBoardByBoardNo(int boardNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDto board = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "select boardno, writer, title, content, writedate, modifydate, readcount, deleted from tbl_board where boardno = ? and deleted = false";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				board = new BoardDto();
				board.setBoardNo(rs.getInt(1));
				board.setWriter(rs.getString(2));
				board.setTitle(rs.getString(3));
				board.setContent(rs.getString(4));
				board.setWriteDate(rs.getDate(5));
				board.setModifyDate(rs.getDate(6));
				board.setReadCount(rs.getInt(7));
				board.setDeleted(rs.getBoolean(8));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception ex) {}
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return board;
		
	}

	@Override
	public void updateBoardReadCount(int boardNo) {
			
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			
			String sql = "update tbl_board set readcount = readcount + 1 where boardno = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			pstmt.executeUpdate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
	}

	@Override
	public void deleteBoardByNo(int boardNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			
			String sql = // "delete from tbl_board where boardno = ?";
						    "update tbl_board set deleted = true where boardno = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			pstmt.executeUpdate();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
	}

	@Override
	public int selectBoardCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int dataCount = 0;		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "select count(*) from tbl_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				dataCount = rs.getInt(1);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception ex) {}
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return dataCount;
	}

	@Override
	public void insertBoardAttach(BoardAttachDto attach) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			String sql = "insert into tbl_boardattach (boardno, userfilename, savedfilename) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attach.getBoardNo());
			pstmt.setString(2, attach.getUserFileName());
			pstmt.setString(3, attach.getSavedFileName());
			pstmt.executeUpdate();
				
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
	}

	@Override
	public ArrayList<BoardAttachDto> selectBoardAttachmentsByBoardNo(int boardNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BoardAttachDto> attachments = new ArrayList<>();
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			
			String sql = "select * from tbl_boardattach where boardno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				 
				BoardAttachDto attach = new BoardAttachDto();
				attach.setAttachNo(rs.getInt(1));
				attach.setBoardNo(rs.getInt(2));
				attach.setUserFileName(rs.getString(3));
				attach.setSavedFileName(rs.getString(4));
				attach.setDownloadCount(rs.getInt(5));
				attachments.add(attach);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return attachments;
	}

	@Override
	public BoardAttachDto selectBoardAttachByAttachNo(int attachNo) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardAttachDto attach = null;
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/labdb", "human", "human");
			
			String sql = "select * from tbl_boardattach where attachno = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attachNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				 
				attach = new BoardAttachDto();
				attach.setAttachNo(rs.getInt(1));
				attach.setBoardNo(rs.getInt(2));
				attach.setUserFileName(rs.getString(3));
				attach.setSavedFileName(rs.getString(4));
				attach.setDownloadCount(rs.getInt(5));
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (Exception ex) {}
			try {conn.close();} catch (Exception ex) {}
		}
		
		return attach;
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
}
