
<%@ page import="www.dream.com.party.model.*"%>

<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<input type='hidden' name='${_csrf.parameterName}' value='${_csrf.token}'>
<input type="hidden" name="id" value="${newbie != null ? newbie.id : 0}" readonly="readonly" class="form-control" >
<div class="form-group row" id="loginId">
	<div class="col-sm-6 mb-3 mb-sm-0">
		<input type="text" class="form-control form-control-user"
			id='inputLoginId' onkeydown="onlyAlphabet(this)" name="loginId"
			placeholder="Google ID" value="${newbie.loginId}">
	</div>
	<div class="col-sm-6 mb-3">
		<span id="changeFont">@gmail.com</span>
	</div>
</div>
<div class="form-group row" id="pw">
	<div class="col-sm-6 mb-3 mb-sm-0">
		<input type="password" class="form-control form-control-user"
			name="password" placeholder="Password" id="pw1">
	</div>
	<div class="col-sm-6 mb-3">
		<input type="password" class="form-control form-control-user"
			placeholder="Repeat Password" id="pw2">
	</div>
</div>
<div class="form-group row" id="names">
	<div class="col-sm-6 mb-3 mb-sm-0">
		<input type="text" class="form-control form-control-user" name="name"
			placeholder="Name" onkeydown="onlyAlphabet(this)" id="name" value="${newbie.name}">
	</div>
	<div class="col-sm-6 mb-3">
		<input type="text" class="form-control form-control-user"
			id='inputNickname' onkeydown="onlyAlphabet(this)" name="nickname" 
			placeholder="Nickname" value="${newbie.nickname}">
	</div>
</div>
<div class="form-group row" id="labelFont">
	<div class="col-sm-6 mb-3 mb-sm-0">
		<span id="changeFont"><i class="fas fa-birthday-cake"></i>BirthDate</span>
	</div>
	<div class="col-sm-6 mb-3">
		<input type="date" name="birthDate" class="form-control form-control-user" min="1950-01-01" id="birthDate"
				 value='<fmt:formatDate value="${newbie.birthDate}" pattern="yyyy-MM-dd" />' >
	</div>
</div>
<div class="form-group row" id="descript">
	<div class="col-sm-6 mb-3 mb-sm-0" id="idDesc">
		<p class="small">ID can contain at least one number digit and one dot( . )
		</p>
	</div>
	<div class="col-sm-6 mb-3">
		<p class="small">Password must contain at least one lowercase letter, number digit and special character(~`!@#$%\^&*()-+=)
		</p>
	</div>
</div>
<script type="text/javascript">
	//CRUD에 대한 기능 readonly, show, hide로 제어함
	//create read update remove + change
	function setOprationMode(oprationMode) {
		if ("read" === oprationMode) {
			$("#inputLoginId").attr("readonly", true);
			$("#pw").hide();
			$("#name").attr("readonly", true);
			$("#inputNickname").attr("readonly", true);	
			$("#birthDate").attr("readonly", true);
			$("#descript").hide();
		} else if ("update" === oprationMode) {
			$("#inputLoginId").attr("readonly", true);
			$("#pw").hide();
			$("#descript").hide();
		} else if ("remove" === oprationMode) {
			$(".form-group").hide();
		} else if ("change" === oprationMode) {
			$("#loginId").hide();
			$("#pw2").hide();
			$("#names").hide();
			$("#labelFont").hide();
			$("#idDesc").hide();
		}
	}
</script>