$(function(){
	//绑定账号的 controller url
	var bindUrl='/o2o/local/bindlocalauth';
	//从地址栏里获取userType,为1则是前端展示系统,其余则为店家管理系统
	var usertype=getQueryString('usertype');
	$('#submit').click(function(){
		//获取用户前台输入的账号
		var userName=$('#username').val();
		//获取用户输入的密码
		var password=$('#psw').val();
		//获取输入的验证码
		var verifyCodeActual=$('#j_captcha').val();
		var needVerify=false;
		if(!verifyCodeActual){
			$.toast('请输入验证码!');
			return;
		}
		
		//用ajax访问后台,绑定账号
		$.ajax({
			url:bindUrl,
			async:false,
			cache:false,
			type:'post',
			dateType:'json',
			data:{
				userName:userName,
				password:password,
				verifyCodeActual:verifyCodeActual
			},
			success:function(data){
				if(data.success){
					$.toast('绑定成功！');
					if(usertype==1){
						//若用户在前端展示页面则自动跳到前端展示页面的首页
						window.location.href='/o2o/frontend/index';
					}else{
						//若是店家则自动跳到店铺列表页面
						window.location.href='/o2o/shopadmin/shoplist';
					}
				}else{
					$.toast('提交失败!'+data.errMsg);
					//刷新验证码
					$('#captcha_img').click();
				}
			}
		
		});
	});

});