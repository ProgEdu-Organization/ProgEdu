<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="utf-8"%>
<%@ page import="fcu.selab.progedu.conn.Language" %>
<%@ page import="java.util.Locale" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	//*****
	// ***選擇語言
	// *****
	Locale locale = request.getLocale();
	String localLan = locale.getLanguage();  // internet Language
	
	String finalLan = localLan;
	String reqLan = request.getParameter("lang");  // request Language
	Cookie lanCookie = null;
	
	String non = "";
	if(null != reqLan && !non.equals(reqLan)){
      finalLan = reqLan;
      lanCookie = new Cookie("lang", finalLan);
  	  response.addCookie(lanCookie);
	}else{
	  Cookie[] lanCookies = request.getCookies();
		if(lanCookies != null){
		  for(Cookie c : lanCookies){
		    if(c.getName().equals("lang")){
		      lanCookie = c;
		      break;
		    }
		  }
		}
		if(lanCookie != null){
		  finalLan = lanCookie.getValue();
		}
	}
	
	Language language = new Language();
	String basename = language.getBaseName(finalLan);
%>
<!-- 設定語言 -->
<fmt:setBundle basename = "<%=basename %>"/>