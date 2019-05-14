
$(function () {
    // var pageNum = 1,
    //     pageSize = 10;
    var colsArr = [

    ]
    layui.use(['table', 'layer', 'laydate'], function () {

        var table = layui.table;
        var laydate = layui.laydate;
        //执行一个 table 实例
        table.render({
            elem: '#tx-list-table',
            width: '100%',
            height: 650,
            maxHeight: '100vh',
            url: 'http://192.168.199.238:8001/api/search', // 数据接口
            method: 'get',
            // contentType: 'application/json',
            title: '通信数据',
            page: true, // 开启分页
            limits: [],  // 每页条数选项
            loading: true,  // 显示加载条
            text: {    // 空数据等异常提示文字
                none: '暂无相关数据', //默认：无数据
            },
            even: true, // 开启各行背景
            size: 'sm', // 小尺寸表格
            toolbar: '#txTable_toolBar', // 开启工具栏，不显示左侧模板
            defaultToolbar: ['filter'],
            // totalRow: true, // 开启合计行
            where: {
                // 额外请求参数
                distance: 50, // 距离
                type: 1,    // 参照
            },
            headers: {
                // 接口请求头
            },
            // request: {
            //     // 自定义请求参数
            //     pageName: 'pageNum',
            //     limitName: 'pageSize',
            // },
            response: {
                // 自定义返回参数格式
                // 自定义数据响应名称
                statusCode: 200
            },
            parseData: function (res) { //res 即为原始返回的数据
                console.log(res);
                var r = JSON.parse(res.model);
                console.log(r)
                return {
                  "code": res.code, //解析接口状态
                  "msg": res.message, //解析提示文本
                //   "count": res.model.totalNum, //解析数据长度
                  "data": r //解析数据列表
                };
            },
            cols: [[ //表头
                // // // 移动2G
                // {field: 'stationName-yd2g', title: 'DMC基站名', width: 250, templet:'<div>{{d.yd4GList.stationName}}</div>'},
                // {field: 'lat-yd2g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.yd4GList.lat)}}</div>'},
                // {field: 'lng-yd2g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.yd4GList.lng)}}</div>'},
                // {field: 'door-yd2g', title: '室内室外', width: 100, templet:'<div>{{d.yd4GList.door}}</div>'},
                // {field: 'manufacturer-yd2g', title: '厂商名称', width: 100, templet:'<div>{{d.yd4GList.manufacturer}}</div>'},
                // 移动2G
                {field: 'stationName-yd2g', title: 'DMC基站名', width: 250, templet:'<div>{{d.stationName}}</div>'},
                {field: 'lat-yd2g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.lat)}}</div>'},
                {field: 'lng-yd2g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.lng)}}</div>'},
                {field: 'door-yd2g', title: '室内室外', width: 100, templet:'<div>{{d.door}}</div>'},
                {field: 'manufacturer-yd2g', title: '厂商名称', width: 100, templet:'<div>{{d.manufacturer}}</div>'},
                // 移动4G
                // {field: 'stationName-yd4g', title: '所属E-NODEB', width: 250, templet:'<div>{{d.stationName}}</div>'},
                // {field: 'lat-yd4g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.lat)}}</div>'},
                // {field: 'lng-yd4g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.lng)}}</div>'},
                // {field: 'door-yd4g', title: '覆盖类型', width: 100, templet:'<div>{{d.door}}</div>'},
                // {field: 'manufacturer-yd4g', title: '厂家名称', width: 100, templet:'<div>{{d.manufacturer}}</div>'},
                // {fixed: 'right', title: '操作', width: 120, align: 'center', toolbar: '#txTable_bar'},
                // // 移动4G
                // {field: 'stationName-yd4g', title: 'DMC基站名', width: 250, templet:'<div>{{d.yd4GList.stationName}}</div>'},
                // {field: 'lat-yd4g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.yd4GList.lat)}}</div>'},
                // {field: 'lng-yd4g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.yd4GList.lng)}}</div>'},
                // {field: 'door-yd4g', title: '室内室外', width: 100, templet:'<div>{{d.yd4GList.door}}</div>'},
                // {field: 'manufacturer-yd4g', title: '厂商名称', width: 100, templet:'<div>{{d.yd4GList.manufacturer}}</div>'},
                // // 电信4G
                // {field: 'stationName-dx4g', title: 'eNodeBName', width: 250, templet:'<div>{{d.dx4GList.stationName}}</div>'},
                // {field: 'lat-dx4g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.dx4GList.lat)}}</div>'},
                // {field: 'lng-dx4g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.dx4GList.lng)}}</div>'},
                // {field: 'door-dx4g', title: '站型', width: 100, templet:'<div>{{d.dx4GList.door}}</div>'},
                // {field: 'manufacturer-dx4g', title: '厂家', width: 100, templet:'<div>{{d.dx4GList.manufacturer}}</div>'},
                // // 联通4G
                // {field: 'stationName-lt4g', title: '基站名称', width: 250, templet:'<div>{{d.lt4GList.stationName}}</div>'},
                // {field: 'lat-lt4g', title: '经度', width: 100, templet:'<div>{{isZerotok(d.lt4GList.lat)}}</div>'},
                // {field: 'lng-lt4g', title: '纬度', width: 100, templet:'<div>{{isZerotok(d.lt4GList.lng)}}</div>'},
                // {field: 'door-lt4g', title: '宏站/室分', width: 100, templet:'<div>{{d.lt4GList.door}}</div>'},
                // {field: 'manufacturer-lt4g', title: '厂家', width: 100, templet:'<div>{{d.lt4GList.manufacturer}}</div>'},
            ]],
        });
        //日期范围
        laydate.render({
            elem: '#reload_reportDate',
            type: 'month',
        });

        // 方法重载
        // 搜索
        var reload_distance = 0;
        var reload_type = 1;
        var $ = layui.$,
        active = {
            reload: function () {
                reload_distance = $('#reload_distance').val()==""?0:$('#reload_distance').val();
                reload_type = $('#reload_type option:selected').attr('type')*1;
                // var 
                //执行重载
                table.reload('tx-list-table', {
                    // page: {
                    //     curr: 1 //重新从第 1 页开始
                    // },
                    where: {
                        distance: reload_distance,
                        type: reload_type,
                    }
                });
            }
        };
        $('#reload_btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //监听行工具事件
        table.on('toolbar(tx-list-table)', function(obj){
            console.log(obj);
            var event = obj.event;
            if(event == 'importExcel'){
                // 导入文件
                importExc();
                $('#articleImageFile').change(function(){
                    var fileLocation = $(this).val();
                    console.log(fileLocation);
                    console.log($('aFile'));
                    $('.poFile span').text(fileLocation);
                })
            }else if(event == 'exportExcel'){
                // 导出文件
                exportExc();

            }
        });

        //监听事件
        table.on('tool(txTable_bar)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            console.log(obj);
            console.log(data);

            if(layEvent === 'detail'){
                // vm.projectNo = data.projectNo;
                // vm.obtainEmpTable();
                // layer.confirm('去添加项目经理', function(index){
                    
                //     layer.msg(index);
                // });
            }
        });

    })
})

// 是否为0→""
function isZerotok(value){
    if(value == 0){
        value = ""
    }
    return value;
}


var canImport = true;
function importExc(){
    // 弹出框
    layer.open({
        type: 1,
        title: ['新建导入', 'font-size: 14px'],
        id: 'lay_exportExcel',
        shadowClose: false, // 点击遮罩层 关闭
        area: ['300px', '200px'],
        content: '<div class="form-group" id="thumbnailUploadContainer" style="clear:both;height: 30px;padding:20px 10px;" class="col-sm-10"><span class="poFile"><input id="articleImageFile" name="excelFile" type="file" class="form-control" /><span>点击选择要上传的文件</span></span></div>',
        resize: false,
        shade: 0.5,
        maxmin: false,
        btn: ['导入','关闭'],
        yes:function(index,layero){

            var formData = new FormData();
            var name = $("#articleImageFile").val();
            console.log(document.getElementById('articleImageFile'));
            console.log($("#articleImageFile"));
            formData.append("file", $("#articleImageFile")[0].files[0]);
            // console.log(formData);
            // console.log(typeof(formData));
            formData.append("name", name); //这个地方可以传递多个参数
            if(canImport){
                canImport = !canImport;
                $.ajax({
                    url: "192.168.199.238:8001/api/upload",
                    type: 'POST',
                    async: true,
                    data: formData,
                    // 告诉jQuery不要去处理发送的数据
                    processData: false,
                    // 告诉jQuery不要去设置Content-Type请求头
                    contentType: false,
                    beforeSend: function () {
                        console.log("正在进行，请稍候");
                    },
                    success: function (r) {
                        console.log(r);
                        canImport = !canImport;
                        if (r.code == 200) {
                            alert(r.message);
                            // parent.location = "/pmp/sysindex.html#biz/poList.html";
                        } else if(r.code == 1){
                            alert(r.message);
                        } else {
                            alert(r.message);
                        }
                    }
                });
            }else{
                alert("正在进行，请稍候");
            }
        },
        btn2:function(index,layero){
            layer.closeAll();
        },
    })
}
