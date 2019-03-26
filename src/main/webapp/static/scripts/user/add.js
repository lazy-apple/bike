$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-user-add").validate({
		rules : {
			username : {
				required:true,
				minlength:2,
				maxlength:16
			},
			sex : {
				required:true,
			}			
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			$(form).ajaxSubmit({
				type: 'post',
				url: "/user",
				success: function(data){
					var index = parent.layer.getFrameIndex(window.name);
					parent.location.reload();
					parent.layer.close(index);
				},
                error: function(XmlHttpRequest, textStatus, errorThrown){
					layer.msg('ERROR!',{icon:1,time:1000});
				}
			});
		}
	});
});