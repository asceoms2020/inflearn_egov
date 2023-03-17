<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Ajax 를 사용하여 문서 로드될 시 버튼 안보이게 하기 가능 -->
    <script type = "text/javascript">
    function init()
    {
    	if("${resultList[0].NICKNAME}"=="${userId}")
    		{
    		}
    	else
    		{
	    	$("#editbtn").hide();
	    	$(".deletebtn").hide();
    		}
    }
    </script>
    
    
    <title>Document</title>
<!--      <script type="text/javascript"> -->
<!--     function editDo(edit) -->
<!--      { -->
<!--      	//이동경로 -->
<%--      	location.href="borderEdit.do?no=${resultList[0].BORDERID}"; --%>
<!--      } -->
<!--     // 알아야하는 이유 : Frontend 와 Backend가 소통하기 위해서 -->
<!--     </script> -->
    <style>
        .mytable
        {
            border: 1px solid black;
            width: 800px;
            margin:50px auto 0px auto;
        }
        
        .mytable .td1
        {
            width:100px;
        }
        .mytable .td2
        {
            width:700px;
        }
        .mytable .td3
        {
            vertical-align: 0px;
        }
        .mytable .td4
        {
            text-align: right;
        }

        .mytable .mytextarea
        {
            height:500px;
            resize: none;
        }
    </style>
</head>
<body onload = "init()">
    <table class="mytable">
        <tr>
            <td class="td1">작성자</td>
            <td class="td2">${resultList[0].NICKNAME}</td>
        </tr>
        <tr>
            <td class="td1">제목</td>
            <td><input type="text" class="td2" name="title" readonly value="${resultList[0].TITLE}"></td>
        </tr>
        <tr>
            <td class="td1 td3">내용</td>
            <td><textarea class="td2 mytextarea" name="mytextarea" readonly>${resultList[0].BORDERTEXT}</textarea></td>
        </tr>
        <tr>
            <td colspan="2" class="td4">
            <!-- javascript로 사용자에게 안보여지게 처리필요. -->
<!--                 <input type="button" value="수정" onClick="editDo()"> -->
<%--                 <a href="borderEdit.do?no=${resultList[0].BORDERID}">	<input type="button" value="수정"></a> --%>
<%--                 <a href="borderRemove.do?no=${resultList[0].BORDERID}">	<input type="button" value="삭제"></a> --%>

                <a href="borderEdit.do?no=${resultList[0].BORDERID}" id="editbtn">	<input type="button" value="수정"></a>
                <a href="borderRemove.do?no=${resultList[0].BORDERID}" class="deletebtn">	<input type="button" value="삭제"></a>
                <a href="borderReply.do?no=${resultList[0].BORDERID}">	<input type="button" value="답글"></a>
                <a href="borderList.do"><input type="button" value="목록보기"></a>
            </td>
        </tr>
    </table>
</body>
</html>
