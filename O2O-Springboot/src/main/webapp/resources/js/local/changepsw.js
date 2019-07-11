$(function() {
	// 修改平台账号密码的controller的url
	var changepswUrl = '/o2o/local/changelocalpwd';
	// 从地址栏里获取usertype
	var usertype = getQueryString('usertype');
	$('#submit').click(function() {
		// 获取账号
		var userName = $('#userName').val();
		// 获取原来的密码
		var password = $('#password').val();
		// 获取要修改后的密码
		var newPassword = $('#newPassword').val();
		// 获取确认密码
		var confirmPassword = $('#confirmPassword').val();
		if (newPassword != confirmPassword) {
			$.toast('两次密码输入不一致');
			return;
		}
		// 添加表单数据
		var formData = new FormData();
		formData.append('userName', userName);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		// 获取验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码');
			return;
		}
		formData.append('verifyCodeActual', verifyCodeActual);
		// 利用ajax发送数据到后台
		$.ajax({
			url : changepswUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功!');
					if (usertype == 1) {
						// 如果用户在前端展示系统中则自动跳到前端展示系统首页页面
						window.location.href = '/o2o/frontend/index';
					} else {
						// 如果店家在店家管理页面则自动跳到店家列表页面
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('提交失败!' + data.errMsg);
					// 刷新验证码
					$('#captcha_img').click();
				}
			}

		});
	});
	//若点击返回按钮则退回到店家列表页面
	$('#back').click(function(){
		window.location.href='/o2o/shopadmin/shoplist';
	
	});
});