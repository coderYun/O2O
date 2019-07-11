$(function() {
	// 获取此店铺下的商品列表的URL
	var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	// 商品下架的URL
	var statusUrl = '/o2o/shopadmin/modifyproduct';
	getList();
	/**
	 * 获取店铺下的商品列表
	 */
	function getList() {
		// 从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			// 如果获取成功，就遍历每条商品信息并显示在前台
			// 拼接成一行商品信息
			// 商品名称 优先级，上架/下架(含productId) 编辑(含productId) 预览(含productId)
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				productList.map(function(item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					// 若商品状态为0，则表示是下架状态，那么操作把它就变为上架状态(即点击上架按钮即可)
					if (item.enableStatus == 0) {
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-33">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.point
							+ '</div>'
							+ '<div class="col-40">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				// 将拼接好的商品信息赋值给HTML控件
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	// 将class为product-wrap里面的a标签绑定相应的事件
	$('.product-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							// 若是edit按钮则进入到商品编辑页面并带有productId参数
							window.location.href = '/o2o/shopadmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
							// 若是class status则调用后台上/下架相关操作，并带有productId参数
						} else if (target.hasClass('status')) {
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							window.location.href = '/o2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}
					});

	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/productoperation';
	});


function changeItemStatus(id, enableStatus) {
	// 定义product json对象并添加productId以及状态()
	var product = {};
	product.productId = id;
	product.enableStatus = enableStatus;
	$.confirm('确定么?', function() {
		$.ajax({
			url : statusUrl,
			type : 'POST',
			data : {
				productStr : JSON.stringify(product),
				statusChange : true
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					$.toast('操作成功！');
					getList();
				} else {
					$.toast('操作失败！');
				}
			}
		});
	});
}
});
