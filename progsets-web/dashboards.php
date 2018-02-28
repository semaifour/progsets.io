<?php
    if(isset($_GET["proc"])) {
        $proc =  $_GET["proc"];
        if($proc=="new") {

        }
        elseif($proc=="edit") {
            if(isset($_GET["dashboardid"])) {
                $dashboardId = $_GET["dashboardid"];
            }
        }
        elseif($proc=="list") {

        }
        elseif($proc=="view") {
            if(isset($_GET["dashboardid"])) {
                $dashboardId = $_GET["dashboardid"];
            }
        }
    }
?>
<script>
    var proc = "<?php if(isset($_GET["proc"])) { echo $proc;} else { echo ""; } ?>";
    var dashboardId = "<?php if(isset($_GET["dashboardid"])) { echo $dashboardId; } else { echo ""; } ?>";
</script>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Dashboards">
    <title>Progsets - Dashboards</title>
    <link href="images/favicon.webp" type="image/webp" rel="icon">
    <script src="js/jquery.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/c3.css" rel="stylesheet">
    <link href="css/app.css" rel="stylesheet">
    <script src="js/d3.v3.min.js"></script>
    <script src="js/c3.min.js"></script>
    <script src="js/config.js"></script>
    <link rel="stylesheet" href="dist/gridstack.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.0/jquery-ui.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/3.5.0/lodash.min.js"></script>
    <script src="dist/gridstack.js"></script>
    <script src="dist/gridstack.jQueryUI.js"></script>
    <script src="js/nav.js"></script>
    <style type="text/css">
        .grid-stack {
            /*background: lightgoldenrodyellow;*/
        }
        .grid-stack-item-content {
            color: #2c3e50;
            text-align: center;
            border: 1px solid #c4c3c3;
            background-color: #fff;
        }
        .container-fluid {
            padding-top: 75px;
            padding-bottom: 20px;
        }
        .graph {
            margin-top: 0;
        }
    </style>
  </head>
  <body>
    <!-- start:loading blocker -->
    <div class="blkr">
        <div class="loader"></div>
        <div class="blocker"></div>
    </div>
    <!-- end:loading blocker -->

    <!-- start:add item popup -->
    <div class="add-item-popup">
        <h4 class="popup-add-item-title">Save dashboard</h4>
        <div class="close-popup">
            <span class="close-icon">
                <svg>
                    <svg id="close" viewBox="0 0 14.728 14.728" width="100%" height="100%"><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435.293a1 1 0 0 1 0 1.415L1.707 14.435a.999.999 0 1 1-1.414-1.414L13.021.293a.999.999 0 0 1 1.414 0z"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435 14.435a.999.999 0 0 1-1.414 0L.293 1.708A1 1 0 1 1 1.707.293l12.728 12.729a.998.998 0 0 1 0 1.413z"></path></svg>
                </svg>
            </span>
        </div>
        <div class="form-group">
            <div class="form-label">Dashboard name</div>
            <input type="text" id="dashboard-name" class="text-input">
        </div>
        <div class="form-group">
            <div class="form-label">Description</div>
            <textarea id="dashboard-description" class="textarea-input"></textarea>
        </div>
        <div class="right">
            <button id="save-grid" class="small primary-btn">Add</button>
        </div>
    </div>
    <!-- start:add item popup -->
    <div class="dashboards-list">
        <h4 class="popup-add-item-title">Visual listing</h4>
        <div class="close-popup">
            <span class="close-icon">
                <svg>
                    <svg id="close" viewBox="0 0 14.728 14.728" width="100%" height="100%"><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435.293a1 1 0 0 1 0 1.415L1.707 14.435a.999.999 0 1 1-1.414-1.414L13.021.293a.999.999 0 0 1 1.414 0z"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435 14.435a.999.999 0 0 1-1.414 0L.293 1.708A1 1 0 1 1 1.707.293l12.728 12.729a.998.998 0 0 1 0 1.413z"></path></svg>
                </svg>
            </span>
        </div>
        <div class="view-list-table-wrap">
            <table class="view-list-table">
                <thead>
                    <tr>
                        <th>Chart Name</th>
                        <th>Description</th>
                        <th>Chart Type</th>
                        <th>Last Modified</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="visualisation-tbody"></tbody>
            </table>
        </div>
    </div>
    <div class="delete-item-popup">
        <svg viewBox="0 0 510 510"><path d="M255,0C114.75,0,0,114.75,0,255s114.75,255,255,255s255-114.75,255-255S395.25,0,255,0z M280.5,382.5h-51v-51h51V382.5z M280.5,280.5h-51v-153h51V280.5z"/></svg>
        <h3>Are you sure want to delete?</h3>
        <div class="delete-wrap">
            <button id="delete-popup-yes" class="small primary-btn danger">Yes</button>
            <button class="small close-popup primary-btn">Cancel</button>
        </div>
    </div>
    <div class="popup-block"></div>
    <!-- end:add item popup -->

    <!-- start:header -->
    <header class="header">
        <div class="dashboard-top-nav">
            <a href="index.php" class="logo-block">
                <!-- <span class="logo-icon">
                    <svg viewBox="0 0 158.19 179">
                        <path d="M135.99 124.56c-10.36 12.49-26 20.44-43.49 20.44-10.12 0-19.62-2.66-27.84-7.32-9 3.66-25.33 4.15-25.33 4.15l12.94-13.66C42.21 117.97 36 103.96 36 88.5 36 57.3 61.3 32 92.5 32c17.2 0 32.61 7.69 42.98 19.83l22.31-20.18C141.38 12.29 116.87 0 89.5 0 40.07 0 0 40.07 0 89.5S40.07 179 89.5 179c27.6 0 52.28-12.49 68.69-32.13l-22.2-22.31z"></path><path d="M115.875 72.25a3.375 3.375 0 0 1-3.375 3.375H69.375a3.375 3.375 0 1 1 0-6.75H112.5a3.375 3.375 0 0 1 3.375 3.375zM115.875 90a3.125 3.125 0 0 1-3.125 3.125H69.125a3.125 3.125 0 1 1 0-6.25h43.625A3.125 3.125 0 0 1 115.875 90zM90.938 107.438a3.563 3.563 0 0 1-3.563 3.563H69.563a3.563 3.563 0 1 1 0-7.126h17.813a3.563 3.563 0 0 1 3.562 3.563z"></path>
                    </svg>
                </span>
                <div class="logo-text">PROGSETS</div> -->
                <img src="images/progsets-logo.png">
            </a>
            <div class="top-nav-items-group">
                <ul id="top-nav" class="top-nav-items"></ul>
            </div>
            <div class="profile">
                <ul class="top-nav-items">
                    <li>
                        <div class="email-profile">demo.account</div>
                        <span class="drop-down">
                            <svg viewBox="0 0 9.5 5.811">
                                <path d="M4.75 5.811L.22 1.28A.75.75 0 1 1 1.281.219l3.469 3.47L8.22.219A.75.75 0 1 1 9.281 1.28L4.75 5.811z"></path>
                            </svg>
                        </span>
                        <div class="drop-down-options">
                            <div class="option-item">Profile</div>
                            <div class="option-item">Sign out</div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </header>
    <!-- end:header -->

    <!-- start:global messages -->
    <div class="global-message-block">
        <div class="message-group">
            <img class="error-icon" src="images/exclamation-mark.svg" alt="">
            <div class="message-block">Oops something went wrong!</div>
        </div>
    </div>
    <!-- end:global messages -->

    <!-- start:screens -->
    <div id="container" class="container dashboards-container">
        <div style="margin-top: 0;" class="dashboard-grid-wrap">
            <div class="container-fluid">
            <div class="save-right">
                <button id="save-dashboard-btn" class="small primary-btn">Save</button>
                <button id="update-dashboard-btn" class="small primary-btn">Update</button>
            </div>
            <div class="grid-stack"></div>
            </div>
        </div>
        <!-- start:saved visualisation list -->
        <section class="grid-list">
            <h4>Dashboards<a class="new-query" href="dashboards.php?proc=new">[New Dashboard]</a></h4>
            <table class="view-list-table">
                <thead>
                    <tr>
                        <th>Dashboard Name</th>
                        <th>Description</th>
                        <th>Type</th>
                        <th>Last Modified</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="dashboard-tbody"></tbody>
            </table>
        </section>
        <!-- end:saved visualisation list -->
    </div>
    <!-- end:screens -->

    <script>
        $(function () {
            var x = 0;
            var addEle;
            var chartcount = 0;
            var charts = [];
            var deleteitem;
            var editBody;

            var loopArray = function(arr) {
                customAlert(arr[x],function(){
                    // set x to next item
                    x++;

                    // any more items in array? continue loop
                    if(x < arr.length) {
                        loopArray(arr);   
                    }
                }); 
            }

            function customAlert(visual,callback) {
                // code to show your custom alert
                // in this case its just a console log
                $(".global-message-block").removeClass("error-m");
                if(visual.visualdefinition!==undefined) {
                    var content = visual.visualdefinition;
                    charttype = content.charttype;
                    querybox = content.querybox;
                    $.ajax({
                        method: "POST",
                        url: api.visualquery,
                        headers: {'authorization': api.auth},
                        contentType: 'text/psql',
                        processData: false,
                        data: querybox,
                        crossDomain: true,
                        timeout: global.timeout
                    })
                    .done(function(e) {
                        if(e.success==true) {
                            columndata = e.body[0].data;
                            var bindtochart = '#chart'+x;
                            if(charttype=="bar"||charttype=="line") {
                                var mergedaxislist = content.xaxis.concat(content.yaxis);
                                var rowsCount = columndata.length;
                                var selectedColumns = [];
                                for(var i=0;i<rowsCount;i++) {
                                    if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                        selectedColumns.push(columndata[i]);
                                    }
                                }
                                var dataObj = {};
                                dataObj["columns"] = selectedColumns;
                                dataObj["type"] = charttype;
                                dataObj["x"] = content.xaxis[0];
                                charts["chart"+x+"definition"] = content;
                                if(dataObj["x"]==undefined) {
                                    charts["chart"+x] = c3.generate({
                                        padding: {
                                            top: 10,
                                            bottom: 10,
                                        },
                                        bindto: bindtochart,
                                        data: dataObj
                                    });
                                }
                                else {
                                    charts["chart"+x] = c3.generate({
                                        padding: {
                                            top: 10,
                                            bottom: 10,
                                        },
                                        bindto: bindtochart,
                                        data: dataObj,
                                        axis: {
                                            x: {
                                                type: 'category',
                                                tick : {
                                                  rotate : 90,
                                                  multiline : false
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                            else if(charttype=="pie") {
                                var mergedaxislist = content.xaxis;
                                var rowsCount = columndata.length;
                                var selectedColumns = [];
                                for(var i=0;i<rowsCount;i++) {
                                    if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                        selectedColumns.push(columndata[i]);
                                    }
                                }
                                var dataObj = {};
                                dataObj["columns"] = selectedColumns;
                                dataObj["type"] = charttype;
                                charts["chart"+x+"definition"] = content;
                                charts["chart"+x] = c3.generate({
                                    padding: {
                                        top: 10,
                                        bottom: 10,
                                    },
                                    bindto: bindtochart,
                                    data: dataObj
                                });
                            }
                            else if(charttype=="table") {
                                var mergedaxislist = content.xaxis;
                                var rowsCount = columndata.length;
                                var selectedColumns = [];
                                for(var i=0;i<rowsCount;i++) {
                                    if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                        selectedColumns.push(columndata[i]);
                                    }
                                }
                                var dataObj = {};
                                dataObj["columns"] = selectedColumns;
                                dataObj["type"] = charttype;
                                charts["chart"+x+"definition"] = content;
                                
                                
                                $(".generated-graph").addClass("table");
                                var rowsCount = selectedColumns.length;
                                var columnsCount = selectedColumns[0].length;
                                var insertafter = "chart"+x;
                                $('<table class="view-list-table">\
                                    <thead>\
                                        <tr id="raw-data-listing-header'+x+'"></tr>\
                                    </thead>\
                                    <tbody id="raw-data-listing-body'+x+'"></tbody>\
                                </table>').insertAfter('#chart'+x);

                                for(var i=0;i<columnsCount;i++) {
                                    if(i==0) {
                                        for(var j=0;j<rowsCount;j++) {
                                            $("#raw-data-listing-header"+x).append('\
                                                <th>'+selectedColumns[j][0]+'</th>\
                                            ');
                                        }
                                    }
                                    else if(i!==0) {
                                        var temp = "";
                                        for(var j=0;j<rowsCount;j++) {
                                            temp = temp+"<td>"+selectedColumns[j][i]+"</td>";
                                        }
                                        $("#raw-data-listing-body"+x).append('<tr>'+temp+'</tr>');
                                    }
                                }
                            }
                            // do callback when ready
                            callback();
                        }
                        else if(e.success==false) {
                            $(".blkr").removeClass("show");
                            $(".message-block").text(e.message);
                            $(".global-message-block").addClass("error-m");
                        }
                    }).fail(function(jqXHR, textStatus) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                        $(".global-message-block").addClass("error-m");
                    });
                }
            }

            if(proc=="view") {
                $(".global-message-block").removeClass("error-m");
                $.ajax({
                    method: "GET",
                    url: api.base+"dashboard/"+dashboardId,
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        var ddata = JSON.parse(e.body.content);
                        this.serializedData1 = ddata.griddata;
                        var options = {};
                        $('.grid-stack').gridstack(options);
                    
                        this.grid = $('.grid-stack').data('gridstack');
    
                        this.loadGrid = function () {
                            this.grid.removeAll();
                            var items = GridStackUI.Utils.sort(this.serializedData1);
                            _.each(items, function (node) {
                                this.grid.addWidget($('<div><div class="grid-stack-item-content"><div class="chart-name-in-dashboard-cell">'+node.visualdefinition.visualidentifier+'</div><div class="graph" id="chart'+chartcount+'"></div></div></div>'),
                                    node.x, node.y, node.width, node.height);
                                    chartcount++;
                            }, this);
                            return false;
                        }.bind(this);
    
                        this.loadGrid();
                        loopArray(ddata.griddata);
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
            }

            if(proc=="edit") {
                $(".global-message-block").removeClass("error-m");

                $.ajax({
                    method: "GET",
                    url: api.base+"visual",
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        var body = e.body;
                        var len = body.length;
                        for(var i=0;i<len;i++) {
                            var metadataObj = JSON.parse(body[i].content);
                            $("#visualisation-tbody").append('\
                            <tr data-id="'+body[i].id+'">\
                            <td>'+empty(body[i].name)+'</td>\
                            <td>'+empty(body[i].desription)+'</td>\
                            <td>'+empty(body[i].type)+'</td>\
                            <td>'+empty(metadataObj.lastmodified)+'</td>\
                            <td>\
                            <span class="add-icon"><svg viewBox="0 0 510 510"><path d="M255,0C114.75,0,0,114.75,0,255s114.75,255,255,255s255-114.75,255-255S395.25,0,255,0z M382.5,280.5h-102v102h-51v-102 h-102v-51h102v-102h51v102h102V280.5z"/></svg></span>\
                            </td>\
                            </tr>\
                            ');
                        }
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
                
                $.ajax({
                    method: "GET",
                    url: api.base+"dashboard/"+dashboardId,
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                        timeout: global.timeout
                    })
                    .done(function(e) {
                        if(e.success==true) {
                            editBody = e.body;
                            var ddata = JSON.parse(e.body.content);
                            
                            $("#dashboard-name").val(ddata.dashboardidentifier);
                            $("#dashboard-description").val(ddata.dashboarddescription);

                            this.serializedData1 = ddata.griddata;
                            
                            var options = {};
                                $('.grid-stack').gridstack(options);
                                
                                this.grid = $('.grid-stack').data('gridstack');
                                
                                this.deleteWidget = function (item) {
                                    var el =item.target.closest(".grid-stack-item");
                                    this.grid.removeWidget(el);
                                    return false;
                                }.bind(this);
                                
                        this.updateGrid = function () {
                            this.serializedData = _.map($('.grid-stack > .grid-stack-item:visible'), function (el) {
                                el = $(el);
                                var node = el.data('_gridstack_node');
                                var visualdef = charts[$(el[0]).find(".graph").attr("id")+"definition"];
                                return {
                                    x: node.x,
                                    y: node.y,
                                    width: node.width,
                                    height: node.height,
                                    visualdefinition:visualdef
                                };
                            }, this);
                            var dashboardObj = {};
                            dashboardObj["griddata"] = this.serializedData;
                            dashboardObj["dashboardidentifier"] = $("#dashboard-name").val();
                            dashboardObj["dashboarddescription"] = $("#dashboard-description").val();
                            editBody.content = JSON.stringify(dashboardObj);
                            $.ajax({
                                method: "PUT",
                                url: api.base+"dashboard/"+dashboardId,
                                headers: {'authorization': api.auth},
                                contentType: 'application/json',
                                processData: false,
                                crossDomain: true,
                                data: JSON.stringify(editBody),
                                timeout: global.timeout
                            })
                            .done(function(e) {
                                if(e.success==true) {
                                    window.location.assign("dashboards.php?proc=list");
                                }
                                else if(e.success==false) {
                                    $(".blkr").removeClass("show");
                                    $(".message-block").text(e.message);
                                    $(".global-message-block").addClass("error-m");
                                }
                            }).fail(function(jqXHR, textStatus) {
                                $(".blkr").removeClass("show");
                                $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                                $(".global-message-block").addClass("error-m");
                            });
                            return false;
                        }.bind(this);

                        this.addWidget = function() {
                            this.grid.addWidget($('<div>\
                                <div class="grid-stack-item-content">\
                                    <div class="add-chart-btn-wrap">\
                                        <button class="add-chart-btn small secondary-btn">Add chart</button>\
                                    </div>\
                                    <div class="delete-widget">\
                                        <span class="delete-widget-icon">\
                                            <svg viewBox="0 0 41.756 41.756">\
                                                <path d="M27.948,20.878L40.291,8.536c1.953-1.953,1.953-5.119,0-7.071c-1.951-1.952-5.119-1.952-7.07,0L20.878,13.809L8.535,1.465 c-1.951-1.952-5.119-1.952-7.07,0c-1.953,1.953-1.953,5.119,0,7.071l12.342,12.342L1.465,33.22c-1.953,1.953-1.953,5.119,0,7.071 C2.44,41.268,3.721,41.755,5,41.755c1.278,0,2.56-0.487,3.535-1.464l12.343-12.342l12.343,12.343 c0.976,0.977,2.256,1.464,3.535,1.464s2.56-0.487,3.535-1.464c1.953-1.953,1.953-5.119,0-7.071L27.948,20.878z"/>\
                                            </svg>\
                                        </span>\
                                    </div>\
                                </div>\
                                <div class="add-cell"><svg viewBox="0 0 22 22"><path d="M19.412,8.381h-5.793V2.588C13.619,1.165,12.423,0,11,0C9.576,0,8.381,1.165,8.381,2.588v5.793H2.588 C1.165,8.381,0,9.576,0,11c0,1.424,1.165,2.619,2.588,2.619h5.793v5.793C8.381,20.835,9.576,22,11,22 c1.423,0,2.619-1.165,2.619-2.588v-5.793h5.793C20.835,13.619,22,12.424,22,11C22,9.576,20.835,8.381,19.412,8.381z"/></svg></div>\
                            </div>'),
                            0, 0, 4, 4, true);
                        }.bind(this);

                        $('#update-dashboard-btn').click(this.updateGrid);

                        this.loadGrid = function () {
                            this.grid.removeAll();
                            var items = GridStackUI.Utils.sort(this.serializedData1);
                            _.each(items, function (node) {
                                this.grid.addWidget($('<div>\
                                        <div class="grid-stack-item-content">\
                                            <div class="chart-name-in-dashboard-cell">'+node.visualdefinition.visualidentifier+'</div>\
                                            <div class="graph" id="chart'+chartcount+'"></div>\
                                            <div class="add-chart-btn-wrap">\
                                            </div>\
                                            <div class="delete-widget">\
                                                <span class="delete-widget-icon">\
                                                    <svg viewBox="0 0 41.756 41.756">\
                                                        <path d="M27.948,20.878L40.291,8.536c1.953-1.953,1.953-5.119,0-7.071c-1.951-1.952-5.119-1.952-7.07,0L20.878,13.809L8.535,1.465 c-1.951-1.952-5.119-1.952-7.07,0c-1.953,1.953-1.953,5.119,0,7.071l12.342,12.342L1.465,33.22c-1.953,1.953-1.953,5.119,0,7.071 C2.44,41.268,3.721,41.755,5,41.755c1.278,0,2.56-0.487,3.535-1.464l12.343-12.342l12.343,12.343 c0.976,0.977,2.256,1.464,3.535,1.464s2.56-0.487,3.535-1.464c1.953-1.953,1.953-5.119,0-7.071L27.948,20.878z"/>\
                                                    </svg>\
                                                </span>\
                                            </div>\
                                        </div>\
                                        <div class="add-cell"><svg viewBox="0 0 22 22"><path d="M19.412,8.381h-5.793V2.588C13.619,1.165,12.423,0,11,0C9.576,0,8.381,1.165,8.381,2.588v5.793H2.588 C1.165,8.381,0,9.576,0,11c0,1.424,1.165,2.619,2.588,2.619h5.793v5.793C8.381,20.835,9.576,22,11,22 c1.423,0,2.619-1.165,2.619-2.588v-5.793h5.793C20.835,13.619,22,12.424,22,11C22,9.576,20.835,8.381,19.412,8.381z"/></svg></div>\
                                    </div>'),
                                    node.x, node.y, node.width, node.height);
                                    chartcount++;
                            }, this);
                            return false;
                        }.bind(this);
    
                        this.loadGrid();
                        $(document).on("click",".delete-widget",this.deleteWidget);
                        $(document).on("click",".add-cell",this.addWidget);
                        loopArray(ddata.griddata);
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
            }

            function js_yyyy_mm_dd_hh_mm_ss () {
                now = new Date();
                year = "" + now.getFullYear();
                month = "" + (now.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
                day = "" + now.getDate(); if (day.length == 1) { day = "0" + day; }
                hour = "" + now.getHours(); if (hour.length == 1) { hour = "0" + hour; }
                minute = "" + now.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
                second = "" + now.getSeconds(); if (second.length == 1) { second = "0" + second; }
                return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            }

            $("#container").addClass(proc);
            $(document).on("click",".delete-icon",function() {
                deleteitem = $(this).closest("tr").data("id");
                $(".delete-item-popup").addClass("show");
            });

            $(document).on("click","#delete-popup-yes",function() {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                $.ajax({
                    method: "DELETE",
                    url: api.base+"dashboard/"+deleteitem,
                    headers: {'authorization': api.auth},
                    contentType: 'text/plain',
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        $(".delete-item-popup").removeClass("show");
                        $(".blkr").removeClass("show");
                        window.location.assign("dashboards.php?proc=list");
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
            });

            if(proc=="list") {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                $.ajax({
                    method: "GET",
                    url: api.base+"dashboard",
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        var body = e.body;
                        var len = body.length;
                        for(var i=0;i<len;i++) {
                            var metadataObj = JSON.parse(body[i].content);
                            $("#dashboard-tbody").append('\
                                <tr data-id="'+body[i].id+'">\
                                    <td>'+empty(body[i].name)+'</td>\
                                    <td>'+empty(body[i].desription)+'</td>\
                                    <td>'+empty(body[i].type)+'</td>\
                                    <td>'+empty(metadataObj.lastmodified)+'</td>\
                                    <td>\
                                        <span class="edit-icon"><svg viewBox="0 0 528.899 528.899"><path d="M328.883,89.125l107.59,107.589l-272.34,272.34L56.604,361.465L328.883,89.125z M518.113,63.177l-47.981-47.981c-18.543-18.543-48.653-18.543-67.259,0l-45.961,45.961l107.59,107.59l53.611-53.611C532.495,100.753,532.495,77.559,518.113,63.177z M0.3,512.69c-1.958,8.812,5.998,16.708,14.811,14.565l119.891-29.069L27.473,390.597L0.3,512.69z"></path></svg></span>|\
                                        <span class="eye-icon"><svg viewBox="0 0 561 561"><path d="M280.5,89.25C153,89.25,43.35,168.3,0,280.5c43.35,112.2,153,191.25,280.5,191.25S517.65,392.7,561,280.5 C517.65,168.3,408,89.25,280.5,89.25z M280.5,408C209.1,408,153,351.9,153,280.5c0-71.4,56.1-127.5,127.5-127.5 c71.4,0,127.5,56.1,127.5,127.5C408,351.9,351.9,408,280.5,408z M280.5,204c-43.35,0-76.5,33.15-76.5,76.5 c0,43.35,33.15,76.5,76.5,76.5c43.35,0,76.5-33.15,76.5-76.5C357,237.15,323.85,204,280.5,204z"/></svg></span>|\
                                        <span class="delete-icon"><svg viewBox="0 0 64 64"><g><g id="Icon-Trash" transform="translate(232.000000, 228.000000)"><polygon class="st0" id="Fill-6" points="-207.5,-205.1 -204.5,-205.1 -204.5,-181.1 -207.5,-181.1    "/><polygon class="st0" id="Fill-7" points="-201.5,-205.1 -198.5,-205.1 -198.5,-181.1 -201.5,-181.1    "/><polygon class="st0" id="Fill-8" points="-195.5,-205.1 -192.5,-205.1 -192.5,-181.1 -195.5,-181.1    "/><polygon class="st0" id="Fill-9" points="-219.5,-214.1 -180.5,-214.1 -180.5,-211.1 -219.5,-211.1    "/><path class="st0" d="M-192.6-212.6h-2.8v-3c0-0.9-0.7-1.6-1.6-1.6h-6c-0.9,0-1.6,0.7-1.6,1.6v3h-2.8v-3     c0-2.4,2-4.4,4.4-4.4h6c2.4,0,4.4,2,4.4,4.4V-212.6" id="Fill-10"/><path class="st0" d="M-191-172.1h-18c-2.4,0-4.5-2-4.7-4.4l-2.8-36l3-0.2l2.8,36c0.1,0.9,0.9,1.6,1.7,1.6h18     c0.9,0,1.7-0.8,1.7-1.6l2.8-36l3,0.2l-2.8,36C-186.5-174-188.6-172.1-191-172.1" id="Fill-11"/></g></g></svg></span>\
                                    </td>\
                                </tr>\
                            ');
                        }
                        $(".blkr").removeClass("show");
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
            }

            $(document).on("click",".eye-icon",function() {
                var clickedViewId = $(this).closest("tr").data("id");
                window.location.assign("dashboards.php?proc=view&dashboardid="+clickedViewId);
            });

            $(document).on("click",".edit-icon",function() {
                var clickedViewId = $(this).closest("tr").data("id");
                window.location.assign("dashboards.php?proc=edit&dashboardid="+clickedViewId);
            });

            $(document).on("click",".add-icon",function() {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                $(addEle).addClass("hide");
                $(addEle).closest(".grid-stack-item-content").append('<div class="chart-name-in-dashboard-cell">'+$(this).closest("tr").find('td:first-child').text()+'</div><div class="graph" id="chart'+chartcount+'"></div>');
                var bindtochart = '#chart'+chartcount;
                var visualId = $(this).closest("tr").data("id");
                
                $.ajax({
                    method: "GET",
                    url: api.base+"visual/"+visualId,
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        temp =e;
                        content = JSON.parse(temp.body.content);
                        charttype = content.charttype;
                        querybox = content.querybox;
                        $.ajax({
                            method: "POST",
                            url: api.visualquery,
                            headers: {'authorization': api.auth},
                            contentType: 'text/psql',
                            processData: false,
                            data: querybox,
                            crossDomain: true,
                            timeout: global.timeout
                        })
                        .done(function(e) {
                            if(e.success==true) {
                                columndata = e.body[0].data;
                                if(charttype=="bar"||charttype=="line") {
                                    var mergedaxislist = content.xaxis.concat(content.yaxis);
                                    var rowsCount = columndata.length;
                                    var selectedColumns = [];
                                    for(var i=0;i<rowsCount;i++) {
                                        if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                            selectedColumns.push(columndata[i]);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
                                    dataObj["x"] = content.xaxis[0];
                                    charts["chart"+chartcount+"definition"] = content;
                                    if(dataObj["x"]==undefined) {
                                        charts["chart"+chartcount] = c3.generate({
                                            padding: {
                                                top: 10,
                                                bottom: 10,
                                            },
                                            bindto: bindtochart,
                                            data: dataObj
                                        });
                                    }
                                    else {
                                        charts["chart"+chartcount] = c3.generate({
                                            padding: {
                                                top: 10,
                                                bottom: 10,
                                            },
                                            bindto: bindtochart,
                                            data: dataObj,
                                            axis: {
                                                x: {
                                                    type: 'category'
                                                }
                                            }
                                        });
                                    }
                                }
                                else if(charttype=="pie") {
                                    var mergedaxislist = content.xaxis;
                                    var rowsCount = columndata.length;
                                    var selectedColumns = [];
                                    for(var i=0;i<rowsCount;i++) {
                                        if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                            selectedColumns.push(columndata[i]);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
                                    charts["chart"+chartcount+"definition"] = content;
                                    charts["chart"+chartcount] = c3.generate({
                                        padding: {
                                            top: 10,
                                            bottom: 10,
                                        },
                                        bindto: bindtochart,
                                        data: dataObj
                                    });
                                }
                                else if(charttype=="table") {
                                    var mergedaxislist = content.xaxis;
                                    var rowsCount = columndata.length;
                                    var selectedColumns = [];
                                    for(var i=0;i<rowsCount;i++) {
                                        if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                            selectedColumns.push(columndata[i]);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
                                    charts["chart"+chartcount+"definition"] = content;
                                    $(".generated-graph").addClass("table");
                                    var rowsCount = selectedColumns.length;
                                    var columnsCount = selectedColumns[0].length;
                                    var insertafter = "chart"+chartcount;
                                    $('<table class="view-list-table">\
                                        <thead>\
                                            <tr id="raw-data-listing-header'+chartcount+'"></tr>\
                                        </thead>\
                                        <tbody id="raw-data-listing-body'+chartcount+'"></tbody>\
                                    </table>').insertAfter('#chart'+chartcount);

                                    for(var i=0;i<columnsCount;i++) {
                                        if(i==0) {
                                            for(var j=0;j<rowsCount;j++) {
                                                $("#raw-data-listing-header"+chartcount).append('\
                                                    <th>'+selectedColumns[j][0]+'</th>\
                                                ');
                                            }
                                        }
                                        else if(i!==0) {
                                            var temp = "";
                                            for(var j=0;j<rowsCount;j++) {
                                                temp = temp+"<td>"+selectedColumns[j][i]+"</td>";
                                            }
                                            $("#raw-data-listing-body"+chartcount).append('<tr>'+temp+'</tr>');
                                        }
                                    }
                                }
                                $(".dashboards-list").removeClass("show");
                                $("body").removeClass("no-scroll");
                                addEle = "";
                                $(".blkr").removeClass("show");
                                chartcount = chartcount+1;
                            }
                            else if(e.success==false) {
                                $(".blkr").removeClass("show");
                                $(".message-block").text(e.message);
                                $(".global-message-block").addClass("error-m");
                            }
                        }).fail(function(jqXHR, textStatus) {
                            $(".blkr").removeClass("show");
                            $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                            $(".global-message-block").addClass("error-m");
                        });
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
            });
            
            function empty(e) {
                if(e==undefined) {
                    return "-";
                }
                else {
                    return e;
                }
            }
            
            $(document).on("click",".close-popup",function() {
                $(".add-item-popup").removeClass("show");
                $(".dashboards-list").removeClass("show");
            });
            
            $('.grid-stack').on('gsresizestop', function(event, elem) {
                charts[$(elem).find(".graph").attr("id")].resize();
            });
            
            $(document).on("click",".add-chart-btn",function() {
                $(".dashboards-list").addClass("show");
                $("body").addClass("no-scroll");
                addEle = $(this);
            });

            $(document).on("click",".close-popup",function() {
                $(".add-item-popup").removeClass("show");
                $("body").removeClass("no-scroll");
                $(".delete-item-popup").removeClass("show");
            });
            
            $(document).on("click","#save-dashboard-btn",function() {
                $(".add-item-popup").addClass("show");
                $("#dashboard-name").focus();
            });

            if(proc=="new") {
                $(".global-message-block").removeClass("error-m");
                $.ajax({
                    method: "GET",
                    url: api.base+"visual",
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        var body = e.body;
                        var len = body.length;
                        for(var i=0;i<len;i++) {
                            var metadataObj = JSON.parse(body[i].content);
                            $("#visualisation-tbody").append('\
                                <tr data-id="'+body[i].id+'">\
                                    <td>'+empty(body[i].name)+'</td>\
                                    <td>'+empty(body[i].desription)+'</td>\
                                    <td>'+empty(body[i].type)+'</td>\
                                    <td>'+empty(metadataObj.lastmodified)+'</td>\
                                    <td>\
                                        <span class="add-icon"><svg viewBox="0 0 510 510"><path d="M255,0C114.75,0,0,114.75,0,255s114.75,255,255,255s255-114.75,255-255S395.25,0,255,0z M382.5,280.5h-102v102h-51v-102 h-102v-51h102v-102h51v102h102V280.5z"/></svg></span>\
                                    </td>\
                                </tr>\
                            ');
                        }
                    }
                    else if(e.success==false) {
                        $(".blkr").removeClass("show");
                        $(".message-block").text(e.message);
                        $(".global-message-block").addClass("error-m");
                    }
                }).fail(function(jqXHR, textStatus) {
                    $(".blkr").removeClass("show");
                    $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                    $(".global-message-block").addClass("error-m");
                });
                var options = {};
                $('.grid-stack').gridstack(options);
                new function () {
                    this.serializedData = [
                        {
                            x: 0,
                            y: 0,
                            width: 4,
                            height: 4
                        }
                    ];

                    this.grid = $('.grid-stack').data('gridstack');

                    this.loadGrid = function () {
                        this.grid.removeAll();
                        var items = GridStackUI.Utils.sort(this.serializedData);
                        _.each(items, function (node) {
                            this.grid.addWidget($('<div>\
                                    <div class="grid-stack-item-content">\
                                        <div class="add-chart-btn-wrap">\
                                            <button class="add-chart-btn small secondary-btn">Add chart</button>\
                                        </div>\
                                        <div class="delete-widget">\
                                            <span class="delete-widget-icon">\
                                                <svg viewBox="0 0 41.756 41.756">\
                                                    <path d="M27.948,20.878L40.291,8.536c1.953-1.953,1.953-5.119,0-7.071c-1.951-1.952-5.119-1.952-7.07,0L20.878,13.809L8.535,1.465 c-1.951-1.952-5.119-1.952-7.07,0c-1.953,1.953-1.953,5.119,0,7.071l12.342,12.342L1.465,33.22c-1.953,1.953-1.953,5.119,0,7.071 C2.44,41.268,3.721,41.755,5,41.755c1.278,0,2.56-0.487,3.535-1.464l12.343-12.342l12.343,12.343 c0.976,0.977,2.256,1.464,3.535,1.464s2.56-0.487,3.535-1.464c1.953-1.953,1.953-5.119,0-7.071L27.948,20.878z"/>\
                                                </svg>\
                                            </span>\
                                        </div>\
                                    </div>\
                                    <div class="add-cell"><svg viewBox="0 0 22 22"><path d="M19.412,8.381h-5.793V2.588C13.619,1.165,12.423,0,11,0C9.576,0,8.381,1.165,8.381,2.588v5.793H2.588 C1.165,8.381,0,9.576,0,11c0,1.424,1.165,2.619,2.588,2.619h5.793v5.793C8.381,20.835,9.576,22,11,22 c1.423,0,2.619-1.165,2.619-2.588v-5.793h5.793C20.835,13.619,22,12.424,22,11C22,9.576,20.835,8.381,19.412,8.381z"/></svg></div>\
                                </div>'),
                                node.x, node.y, node.width, node.height);
                        }, this);
                        return false;
                    }.bind(this);

                    this.deleteWidget = function (item) {
                        var el =item.target.closest(".grid-stack-item");
                        this.grid.removeWidget(el);
                        return false;
                    }.bind(this);

                    this.saveGrid = function () {
                        this.serializedData = _.map($('.grid-stack > .grid-stack-item:visible'), function (el) {
                            el = $(el);
                            var node = el.data('_gridstack_node');
                            var visualdef = charts[$(el[0]).find(".graph").attr("id")+"definition"];
                            return {
                                x: node.x,
                                y: node.y,
                                width: node.width,
                                height: node.height,
                                visualdefinition:visualdef
                            };
                        }, this);
                        var dashboardObj = {};
                        dashboardObj["griddata"] = this.serializedData;
                        dashboardObj["dashboardidentifier"] = $("#dashboard-name").val();
                        dashboardObj["dashboarddescription"] = $("#dashboard-description").val();
                        $.ajax({
                            method: "POST",
                            url: api.base+"dashboard?name="+dashboardObj["dashboardidentifier"],
                            headers: {'authorization': api.auth},
                            contentType: 'text/plain',
                            processData: false,
                            crossDomain: true,
                            data: JSON.stringify(dashboardObj),
                            timeout: global.timeout
                        })
                        .done(function(e) {
                            if(e.success==true) {
                                window.location.assign("dashboards.php?proc=list");
                            }
                            else if(e.success==false) {
                                $(".blkr").removeClass("show");
                                $(".message-block").text(e.message);
                                $(".global-message-block").addClass("error-m");
                            }
                        }).fail(function(jqXHR, textStatus) {
                            $(".blkr").removeClass("show");
                            $(".message-block").text("Request timed out after "+global.timeoutinmutes+" minutes.");
                            $(".global-message-block").addClass("error-m");
                        });
                        return false;
                    }.bind(this);

                    this.clearGrid = function () {
                        this.grid.removeAll();
                        return false;
                    }.bind(this);

                    this.addWidget = function() {
                        this.grid.addWidget($('<div>\
                            <div class="grid-stack-item-content">\
                                <div class="add-chart-btn-wrap">\
                                    <button class="add-chart-btn small secondary-btn">Add chart</button>\
                                </div>\
                                <div class="delete-widget">\
                                    <span class="delete-widget-icon">\
                                        <svg viewBox="0 0 41.756 41.756">\
                                            <path d="M27.948,20.878L40.291,8.536c1.953-1.953,1.953-5.119,0-7.071c-1.951-1.952-5.119-1.952-7.07,0L20.878,13.809L8.535,1.465 c-1.951-1.952-5.119-1.952-7.07,0c-1.953,1.953-1.953,5.119,0,7.071l12.342,12.342L1.465,33.22c-1.953,1.953-1.953,5.119,0,7.071 C2.44,41.268,3.721,41.755,5,41.755c1.278,0,2.56-0.487,3.535-1.464l12.343-12.342l12.343,12.343 c0.976,0.977,2.256,1.464,3.535,1.464s2.56-0.487,3.535-1.464c1.953-1.953,1.953-5.119,0-7.071L27.948,20.878z"/>\
                                        </svg>\
                                    </span>\
                                </div>\
                            </div>\
                            <div class="add-cell"><svg viewBox="0 0 22 22"><path d="M19.412,8.381h-5.793V2.588C13.619,1.165,12.423,0,11,0C9.576,0,8.381,1.165,8.381,2.588v5.793H2.588 C1.165,8.381,0,9.576,0,11c0,1.424,1.165,2.619,2.588,2.619h5.793v5.793C8.381,20.835,9.576,22,11,22 c1.423,0,2.619-1.165,2.619-2.588v-5.793h5.793C20.835,13.619,22,12.424,22,11C22,9.576,20.835,8.381,19.412,8.381z"/></svg></div>\
                        </div>'),
                        0, 0, 4, 4, true);
                    }.bind(this);

                    $('#save-grid').click(this.saveGrid);
                    $('#load-grid').click(this.loadGrid);
                    $('#clear-grid').click(this.clearGrid);

                    this.loadGrid();
                    $(document).on("click",".delete-widget",this.deleteWidget);
                    $(document).on("click",".add-cell",this.addWidget);
                };
            }
        });
    </script>
</body>
</html>
