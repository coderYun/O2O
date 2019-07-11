$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过最大则禁止访问后台
	var maxItems = 999;
	// 一页返回的最大条数
	var pageSize = 10;
	// 获取店铺列表的URL
	var listUrl = '/o2o/frontend/listshops';
	// 获取店铺类别列表以及区域列表的URL
	var searchDivUrl = '/o2o/frontend/listshopspageinfo';
	// 页码
	var pageNum = 1;
	// 从地址栏里面尝试获取parentId
	var parentId =getQueryString('parentId');
	var areaId = '';
	var shopCategoryId = '';
	var shopName = '';

	
	/**
	 * 获取店铺类别列表以及区域类别列表
	 */
	function getSearchDivData() {
		// 如果传入了一级类别ID，则取出该一级类别ID下面的所有二级类别店铺列表
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								// 获取从后台返回回来的店铺类别列表
								var shopCategoryList = data.shopCategoryList;
								var html = '';
								html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
								shopCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-category-id='
													+ item.shopCategoryId
													+ '>'
													+ item.shopCategoryName
													+ '</a>';
										});
								//将拼接好的类别列表标签赋值给HTML控件
								$('#shoplist-search-div').html(html);
								var selectOptions = '<option value="">全部街道</option>';
								//获取从后台返回回来的区域类别列表
								var areaList = data.areaList;
								//遍历区域列表列表，拼接出option标签集
								areaList.map(function(item, index) {
									selectOptions += '<option value="'
											+ item.areaId + '">'
											+ item.areaName + '</option>';
								});
								//将拼接好的标签集赋值给HTML控件
								$('#area-search').html(selectOptions);
							}
						});
	}

	// 渲染出店铺类别列表以及区域类别列表以供搜索
	getSearchDivData()

	
	
	/**
	 * 获取分页展示的店铺列表信息
	 * 
	 */

	function addItems(pageSize, pageIndex) {
		// 拼接出查询的URL，有值就拼接进去组合查询，若空值的话就默认去掉该查询的限制
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&parentId=' + parentId + '&areaId=' + areaId
				+ '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
		//设置加载符，若后台还在取数据在不能再次访问后台，避免多次重复加载
		loading = true;
		
		//访问后台获取相应查询条件下的店铺列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取相应查询条件下的店铺列表的总数
				maxItems = data.count;
				var html = '';
				//遍历店铺列表，拼接出卡片集合
				data.shopList.map(function(item, index) {
					html += '' + '<div class="card" data-shop-id="'
							+ item.shopId + '">' + '<div class="card-header">'
							+ item.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.shopImg + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.shopDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				//将拼接好的卡片集合赋值给HTML控件里面去
				$('.list-div').append(html);
				//获取目前已经显示的卡片总数，包过之前已经加载了的
				var total = $('.list-div .card').length;
				//若总数达到跟按此条件查询的数量一样，则停止后台的加载
				if (total >= maxItems) {
				
					// 隱藏加载提示符
					$('.infinite-scroll-preloader').hide();
				}else{
					$('.infinite-scroll-preloader').show();
				}
				//否则页码+1，继续加载出新的店铺
				pageNum += 1;
				//加载结束了就可以再次加载了
				loading = false;
				//刷新页面，显示新的加载出的店铺
				$.refreshScroller();
			}
		});
	}
	// 预先加载十条店铺信息
	addItems(pageSize, pageNum);

	
    //下滑屏幕自动分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
	
    //点击进入店铺的详情页面
	$('.shop-list').on('click', '.card', function(e) {
		var shopId = e.currentTarget.dataset.shopId;
		window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
	});
 
	//选择新的店铺类别之后，重置页码，清空原先的店铺列表，按照新的类别去查询
	$('#shoplist-search-div').on(
			'click',
			'.button',
			function(e) {
				if (parentId) {// 如果传递过来的是一个父类下的子类
					shopCategoryId = e.target.dataset.categoryId;
					//若之前选择了别的category,则移除选定结果，改成选定新的
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						shopCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					//由于查询条件改变，清空店铺列表在进行查询
					$('.list-div').empty();
					//重置页码
					pageNum = 1;
					addItems(pageSize, pageNum);
				} else {// 如果传递过来的父类为空，则按照父类查询
					parentId = e.target.dataset.categoryId;
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						parentId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					//由于查询条件改变，清空店铺列表在进行查询
					$('.list-div').empty();
					//重置页码
					pageNum = 1;
					addItems(pageSize, pageNum);
					parentId = '';
				}

			});
  //需要查询的店铺名字发生变化后，则重置页码，清空原来的店铺列表，在按照新的查询条件进行查询
	$('#search').on('change', function(e) {
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	//区域信息发生变化后，则重置页码，清空原来的店铺列表，在按照新的查询条件进行查询
	$('#area-search').on('change', function() {
		areaId = $('#area-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
   //点击打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
   //初始化页面
	$.init();
});
