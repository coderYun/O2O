$(function() {
	// 定义加载符
	var loading = false;
	// 分页允许返回的最大条数，超过就禁止访问后台
	var maxItems = 20;
	// 默认一页返回的商品总数
	var pageSize = 10;
	// 展示商品列表的URL
	var listUrl = '/o2o/frontend/listproductsbyshop';
	// 默认的页码
	var pageNum = 1;
	// 从地址栏里获取shopid
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';
	// 获取本店铺下面的商品详情信息URL
	var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;
	// 渲染出店铺信息以及商品类别列表信息
	getSearchDivData();
	// 预先加载10条商品信息
	addItems(pageSize, pageNum);
	//給兑换奖品的a标签赋值兑换奖品的url
	$('#exchangelist').attr('href','/o2o/frontend/awardlist?shopId='+shopId);
	//获取本店铺的商品类别列表
	function getSearchDivData() {
		var url = searchDivUrl;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								var shop = data.shop;
								$('#shop-cover-pic').attr('src', getContextPath()+shop.shopImg);
								$('#shop-update-time').html(
										new Date(shop.lastEditTime)
												.Format("yyyy-MM-dd"));
								$('#shop-name').html(shop.shopName);
								$('#shop-desc').html(shop.shopDesc);
								$('#shop-addr').html(shop.shopAddr);
								$('#shop-phone').html(shop.phone);
								// 获取后台返回的该店铺下的商品类别列表
								var productCategoryList = data.productCategoryList;
								var html = '';
								// 遍历商品列表
								productCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-product-search-id='
													+ item.productCategoryId
													+ '>'
													+ item.productCategoryName
													+ '</a>';
										});
								// 将拼接好的商品列表赋值给前端HTML控件
								$('#shopdetail-button-div').html(html);
							}
						});
	}

	/**
	 * 获取分页展示的商品列表信息
	 */

	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML，拼接出查询的URL
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&productCategoryId=' + productCategoryId
				+ '&productName=' + productName + '&shopId=' + shopId;
		// 设定加载符，若还在后台取数据则禁止访问后台，避免多次重复访问加载
		loading = true;

		// 访问后台获取相应查询条件下的商品列表
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取当前查询条件下的商品总数
				maxItems = data.count;
				var html = '';
				// 遍历商品列表,拼接出商品集合
				data.productList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.productName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'+getContextPath()
							+ item.imgAddr + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.productDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				// 将拼接好的商品集合赋值给前端HTML控件
				$('.list-div').append(html);
				// 获取目前已加载的商品总数，包过之前已经加载了的
				var total = $('.list-div .card').length;
				// 若总数达到按此条件下查询出来的总数一致，则停止加载
				if (total >= maxItems) {

					// 隱藏加载提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}
				// 否则继续加载出新的店铺,页码加1
				pageNum += 1;
				// 加载结束可以再次加载了
				loading = false;
				// 刷新页面，显示出新加载的店铺
				$.refreshScroller();
			}
		});
	}

	// 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
	// 选择新的商品类别之后，重置页码，清空原来的商品列表，按照新的类别去查询
	$('#shopdetail-button-div').on(
			'click',
			'.button',
			function(e) {
				productCategoryId = e.target.dataset.productSearchId;
				if (productCategoryId) {
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
				}
			});
	// 点击卡片进入到某个商品的详情页
	$('.list-div')
			.on(
					'click',
					'.card',
					function(e) {
						var productId = e.currentTarget.dataset.productId;
						window.location.href = '/o2o/frontend/productdetail?productId='
								+ productId;
					});
	// 若输入的名字发生变化，重置页码，清空原来的商品列表，按照新的名字去查询
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
});
