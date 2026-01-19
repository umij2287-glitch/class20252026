use labdb;
create table tbl_boardattach
(
    attachno int primary key auto_increment,
    boardno int not null,
    userfilename varchar(100) not null, -- 사용자가 업로드한 파일 이름.
    savedfilename varchar(100) not null, -- 고유한 파일 이름.
    downloadcount int default (0),
    constraint fk_board_boardattach foreign key (boardno) references tbl_board(boardno)
);