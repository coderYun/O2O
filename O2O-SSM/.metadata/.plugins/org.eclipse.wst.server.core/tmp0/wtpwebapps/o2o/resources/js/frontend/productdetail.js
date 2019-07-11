$(function() {
	var productId = getQueryString('productId');
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId='
			+ productId;

	$
			.getJSON(
					productUrl,
					function(data) {
						if (data.success) {
							var product = data.product;
							$('#product-img').attr('src', product.imgAddr);
							$('#product-time').text(
									new Date(product.lastEditTime)
											.Format("yyyy-MM-dd"));
							if(product.point!=undefined){
							$('#product-point').text('购买可得'+product.point+'积分');
							}
							//商品名称
							$('#product-name').text(product.productName);
							//商品描述
							$('#product-desc').text(product.productDesc);
							
							//商品价格展示逻辑
							if(product.normalPrice!=undefined && product.promotionPrice!=undefined){
								//如果现价和原价都不为空，则都展示出来，并给原价加一个删除符号
								$('#price').show();
								$('#normalPrice').html('<del>'+'￥'+product.normalPrice+'</del>');
								$('#promotionPrice').text('￥'+product.promotionPrice);
								
								
							}
							var imgListHtml = '';
							product.productImgList.map(function(item, index) {
								imgListHtml += '<div> <img src="'
										+ item.imgAddr + '"/></div>';
							});
							// 生成购买商品的二维码供商家扫描
							imgListHtml += '<div> <img src="/o2o/frontend/generateqrcode4product?productId='
									+ product.productId + '"/></div>';
							$('#imgList').html(imgListHtml);
						}
					});
	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
