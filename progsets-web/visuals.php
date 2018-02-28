<?php
    if(isset($_GET["proc"])) {
        $proc =  $_GET["proc"];
        if($proc=="new") {
            if(isset($_GET["queryid"])) {
                $queryId = $_GET["queryid"];
            }
        }
        elseif($proc=="edit") {
            if(isset($_GET["id"])) {
                $editId = $_GET["id"];
            }
        }
        elseif($proc=="list") {

        }
        elseif($proc=="view") {
            if(isset($_GET["visualid"])) {
                $visualId = $_GET["visualid"];
            }
        }
    }
?>
<script>
    var proc = "<?php if(isset($_GET["proc"])) { echo $proc;} else { echo ""; } ?>";
    var queryId = "<?php if(isset($_GET["queryid"])) { echo $queryId; } else { echo ""; } ?>";
    var visualId = "<?php if(isset($_GET["visualid"])) { echo $visualId; } else { echo ""; } ?>";

    if(proc=="edit") {
        var editId = "<?php if(isset($_GET["id"])) { echo $editId; } else { echo ""; } ?>";
    }
</script>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Visuals">
    <title>Progsets - Visualizations</title>
    <link rel="icon" href="">
    <script src="js/jquery.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/c3.css" rel="stylesheet">
    <link href="css/app.css" rel="stylesheet">
    <script src="js/d3.v3.min.js"></script>
    <script src="js/c3.min.js"></script>
    <script src="js/nav.js"></script>
    <script src="js/config.js"></script>
    <script src="js/interact.min.js"></script>
    <link href="images/favicon.webp" type="image/webp" rel="icon">
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
        <h4 class="popup-add-item-title">Save visualisation</h4>
        <div class="close-popup">
            <span class="close-icon">
                <svg>
                    <svg id="close" viewBox="0 0 14.728 14.728" width="100%" height="100%"><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435.293a1 1 0 0 1 0 1.415L1.707 14.435a.999.999 0 1 1-1.414-1.414L13.021.293a.999.999 0 0 1 1.414 0z"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435 14.435a.999.999 0 0 1-1.414 0L.293 1.708A1 1 0 1 1 1.707.293l12.728 12.729a.998.998 0 0 1 0 1.413z"></path></svg>
                </svg>
            </span>
        </div>
        <div class="form-group">
            <div class="form-label">Visual name</div>
            <input type="text" id="visual-name" class="text-input">
        </div>
        <div class="form-group">
            <div class="form-label">Description</div>
            <textarea id="visual-description" class="textarea-input"></textarea>
        </div>
        <div class="right">
            <button id="add-graph-btn" class="small primary-btn">Add</button>
        </div>
    </div>

    <div class="edit-code-popup">
        <h4 class="popup-add-item-title">Update json</h4>
        <div class="close-popup">
            <span class="close-icon">
                <svg>
                    <svg id="close" viewBox="0 0 14.728 14.728" width="100%" height="100%"><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435.293a1 1 0 0 1 0 1.415L1.707 14.435a.999.999 0 1 1-1.414-1.414L13.021.293a.999.999 0 0 1 1.414 0z"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435 14.435a.999.999 0 0 1-1.414 0L.293 1.708A1 1 0 1 1 1.707.293l12.728 12.729a.998.998 0 0 1 0 1.413z"></path></svg>
                </svg>
            </span>
        </div>
        <div class="form-group">
            <div class="form-label">Data</div>
            <textarea id="edit-json" class="textarea-input"></textarea>
        </div>
        <div class="right">
            <button id="update-json-btn" class="small primary-btn">Update</button>
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
                <img src="images/progsets-logo.png">
            </a>
            <div class="top-nav-items-group">
                <ul id="top-nav" class="top-nav-items"></ul>
            </div>
            <div class="profile">
                <ul class="top-nav-items">
                    <!--
                    <li>
                        <span class="nav-list-item-content">DOCS</span>
                        <span class="drop-down">
                            <svg viewBox="0 0 9.5 5.811">
                                <path d="M4.75 5.811L.22 1.28A.75.75 0 1 1 1.281.219l3.469 3.47L8.22.219A.75.75 0 1 1 9.281 1.28L4.75 5.811z"></path>
                            </svg>
                        </span>
                    </li>
                    -->
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
    <div id="container" class="container visual-container">
        <!-- chart tabs section start-->
        <div class="chart-tabs-section restrict-width">
            <div class="chart-tabs-block">
                <!-- chart tab start-->
                <div id="step2" class="chart-tab success">
                    <img class="chart-tab-icon" src="images/question2.svg" alt="chart-tab-icon" />
                    <div class="chart-tab-text">Query</div>
                </div>
                <div id="step1" class="chart-tab active">
                    <img class="chart-tab-icon small-icon" src="images/pick.svg" alt="chart-tab-icon" />
                    <div class="chart-tab-text">Visualize</div>
                </div>
                <div id="step3" class="chart-tab">
                    <img class="chart-tab-icon" src="images/dashboard.svg" alt="chart-tab-icon" />
                    <div class="chart-tab-text">Dashboard</div>
                </div>
                <div id="step4" class="chart-tab">
                    <img class="chart-tab-icon" src="images/share4.svg" alt="chart-tab-icon" />
                    <div class="chart-tab-text">Share</div>
                </div>
                <!-- chart tab end-->
            </div>
        </div>
        <!-- chart tabs section end-->

        <!-- start:select chart type -->
        <section class="visual-select">
            <div class="sub-title">Let's graph</div>
            <h4>Select Chart Type </h4>
            <div class="select-visual-type">
                <ul class="charts">
                    <li>
                        <img src="images/bar.png" alt="">
                        <div data-chart-type="bar" class="chart-label">Bar</div>
                        <div class="chart-description">Plots data with rectangular bars with heights proportional to the values.</div>
                    </li>
                    <li>
                        <img src="images/pie.png" alt="">
                        <div data-chart-type="pie" class="chart-label">Pie</div>
                        <div class="chart-description">Circular graphic which is divided into slices to illustrate numerical proportion</div>
                    </li>
                    <li>
                        <img src="images/line.png" alt="">
                        <div data-chart-type="line" class="chart-label">Line</div>
                        <div class="chart-description">Series of data points called 'markers' connected by straight line segments</div>
                    </li>
                    <li>
                        <img src="data:image/png;base64,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" alt="">
                        <div data-chart-type="table" class="chart-label">Table</div>
                        <div class="chart-description">Its collection of data held in a structured format consists of columns, and rows.</div>
                    </li>
                </ul>
            </div>
        </section>
        <!-- end:select chart type -->

        <!-- start:choose columns -->
        <section class="choose-columns-section">
            <div class="query-description">Select data to display on graph</div>
            <h4>Choose columns</h4>
            <div class="choose-columns-wrap">
                <div class="columns-list">
                    <div class="columns-list-header">
                        <span class="database-icon">
                            <svg>
                                <svg viewBox="0 0 512 512" width="100%" height="100%"><path d="M256,0C137.44,0,48,41.28,48,96v320c0,54.72,89.44,96,208,96s208-41.28,208-96V96C464,41.28,374.56,0,256,0z M432,416
            c0,30.24-75.2,64-176,64S80,446.24,80,416V308.48C116.16,335.04,180,352,256,352s139.84-16.96,176-43.52V416z M432,256
            c0,30.24-75.2,64-176,64S80,286.24,80,256V148.48C116.16,175.04,180,192,256,192s139.84-16.96,176-43.52V256z M256,160
            c-100.8,0-176-33.76-176-64s75.2-64,176-64s176,33.76,176,64S356.8,160,256,160z"/></svg>
                            </svg>
                        </span>
                        <span class="content">All Columns</span>
                    </div>
                    <ul id="draggable-list"></ul>
                </div>
                <div class="columns-box">
                    <div class="graph-box-outline-wrap">
                        <div class="columns-y-axis-box dropzone">
                            <ul class="y-axis-dropzone-list"></ul>
                        </div>
                        <div class="graph-box-placeholder">
                            <div class="graph-x-note">x-axis</div>
                            <div class="graph-y-note">y-axis</div>
                        </div>
                    </div>
                    <div class="columns-x-axis-box dropzone">
                        <ul class="x-axis-dropzone-list"></ul>
                    </div>
                    <div class="pie-blank-wrap">
                        <div class="pie-blank"></div>
                        <span class="table-blank-icon">
                            <svg width="533.333px" height="343.334px" viewBox="0 0 533.333 533.334"><path d="M0,83.333V450h533.333V83.333H0z M200,316.667V250h133.333v66.667H200z M333.333,350v66.667H200V350H333.333z M333.333,150
        v66.667H200V150H333.333z M166.667,150v66.667H33.333V150H166.667z M33.333,250h133.333v66.667H33.333V250z M366.667,250H500
        v66.667H366.667V250z M366.667,216.667V150H500v66.667H366.667z M33.333,350h133.333v66.667H33.333V350z M366.667,416.667V350H500
        v66.667H366.667z"/></svg>
                        </span>
                    </div>
                </div>
            </div>
            <div class="apply-columns-select-wrap">
                <button id="apply-selected-columns-btn" class="primary-btn">Apply</button>
            </div>
        </section>
        <!-- end:extract data -->

        <!-- start:saved visualisation list -->
        <section class="grid-list">
            <h4>Visualizations</h4>
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
        </section>
        <!-- end:saved visualisation list -->
        <section class="generated-graph">
            <img class="back-btn" src="images/left-arrow.png" alt="">
            <h4 id="chart-name-title">Visualisation</h4>
            <div class="graph" id="chart"></div>
            <table class="view-list-table visual-table-view">
                <thead>
                    <tr id="raw-data-listing-header"></tr>
                </thead>
                <tbody id="raw-data-listing-body"></tbody>
            </table>
            <div class="apply-columns-select-wrap">
                <button id="save-visualisation-btn" class="primary-btn">Save</button>
            </div>
        </section>
    </div>
    <!-- end:screens -->
    <script>
        var jsonQuery;
        var visualobj = {};
        var temp;
        var content;
        var deleteitem;
        var clickedCodeedit;
        $(document).ready(function () {
            if(proc=="view") {
                $(".global-message-block").removeClass("error-m");
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
                        $("#chart-name-title").text(temp.body.name+" - "+temp.body.desription);
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
                                // e = JSON.parse(e);
                                // columndata = e.body["data"];
                                columndata = e.body[0].data;
                                if(charttype=="bar"||charttype=="line") {
                                    var mergedaxislist = content.xaxis.concat(content.yaxis);
                                    var rowsCount = columndata.length;
                                    var selectedColumns = [];
                                    for(var i=0;i<rowsCount;i++) {
                                        if(mergedaxislist.indexOf(columndata[i][0])!==-1) {
                                            selectedColumns.push(columndata[i]);
                                            // columndata.splice(columndata.indexOf(columndata[i]), 1);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
                                    dataObj["x"] = content.xaxis[0];
                                    if(dataObj["x"]==undefined) {
                                        var chart = c3.generate({
                                            bindto: '#chart',
                                            data: dataObj
                                        });
                                    }
                                    else {
                                        var chart = c3.generate({
                                            bindto: '#chart',
                                            data: dataObj,
                                            axis: {
                                                x: {
                                                    type: 'category',
                                                    tick: {
                                                      rotate: 50,
                                                      multiline: false
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
                                            // columndata.splice(columndata.indexOf(columndata[i]), 1);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
                                    var chart = c3.generate({
                                        bindto: '#chart',
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
                                            // columndata.splice(columndata.indexOf(columndata[i]), 1);
                                        }
                                    }
                                    var dataObj = {};
                                    dataObj["columns"] = selectedColumns;
                                    dataObj["type"] = charttype;
        
                                    $(".generated-graph").addClass("table");
                                    var rowsCount = selectedColumns.length;
                                    var columnsCount = selectedColumns[0].length;
                                    
                                    for(var i=0;i<columnsCount;i++) {
                                        if(i==0) {
                                            for(var j=0;j<rowsCount;j++) {
                                                $("#raw-data-listing-header").append('\
                                                    <th>'+selectedColumns[j][0]+'</th>\
                                                ');
                                            }
                                        }
                                        else if(i!==0) {
                                            var temp = "";
                                            for(var j=0;j<rowsCount;j++) {
                                                temp = temp+"<td>"+selectedColumns[j][i]+"</td>";
                                            }
                                            $("#raw-data-listing-body").append('<tr>'+temp+'</tr>');
                                        }
                                    }
                                    var containerInnerHeight = window.innerHeight;
                                    $(".view-list-table.visual-table-view").height(containerInnerHeight-460);
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
            $(document).on("click",".delete-drop",function() {
                var eleValue = $(this).closest("li").find("span").text();
                $(this).closest("li").remove();
                $("#draggable-list").append('<li class="draggable">'+eleValue+'</li>');
            });

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

            $(document).on("click","#add-graph-btn",function() {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                visualobj["visualidentifier"] = $("#visual-name").val();
                visualobj["visualdescription"] = $("#visual-description").val();
                visualobj["querydefinition"] = jsonQuery;
                visualobj["lastmodified"] = js_yyyy_mm_dd_hh_mm_ss();
                $.ajax({
                    method: "POST",
                    url: api.base+"visual?name="+visualobj["visualidentifier"],
                    headers: {'authorization': api.auth},
                    contentType: 'text/plain',
                    processData: false,
                    crossDomain: true,
                    data: JSON.stringify(visualobj),
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        window.location.assign("visuals.php?proc=list");
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

            $(document).on("click",".close-popup",function() {
                $(".add-item-popup").removeClass("show");
                $(".delete-item-popup").removeClass("show");
                $(".edit-code-popup").removeClass("show");
            });

            $(document).on("click",".edit-code",function() {
                $(".blkr").addClass("show");
                clickedCodeedit = $(this).closest("tr").data("id");
                $.ajax({
                    method: "GET",
                    url: api.base+"visual/"+clickedCodeedit,
                    headers: { 'authorization': api.auth },
                    processData: false,
                    crossDomain: true,
                    data: {},
                    timeout: global.timeout
                })
                .done(function(e) {
                    if(e.success==true) {
                        var body = e.body;
                        $(".edit-json").val(body.content);
                        $(".blkr").removeClass("show");
                        $(".edit-code-popup").addClass("show");
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

            $(document).on("click",".edit-icon",function() {
                var clickedEditId = $(this).closest("tr").data("id");
                window.location.assign("visuals.php?proc=edit&id="+clickedEditId);
            });

            $(document).on("click",".delete-icon",function() {
                deleteitem = $(this).closest("tr").data("id");
                $(".delete-item-popup").addClass("show");
            });
            
            $(document).on("click","#delete-popup-yes",function() {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                $.ajax({
                    method: "DELETE",
                    url: api.base+"visual/"+deleteitem,
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
                        window.location.assign("visuals.php?proc=list");
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

            var columndata;
            var charttype;
            function render() {
                if(charttype=="bar"||charttype=="line") {
                    var yaxislistarray = [];
                    var yaxislist = $(".y-axis-dropzone-list li");
                    var len = yaxislist.length;
                    for(var i=0;i<len;i++) {
                        yaxislistarray.push($(yaxislist[i]).find("span").text());
                    }
                    var xaxislistarray = [];
                    var xaxislist = $(".x-axis-dropzone-list li");
                    var len = xaxislist.length;
                    for(var i=0;i<len;i++) {
                        xaxislistarray.push($(xaxislist[i]).find("span").text());
                    }
                    visualobj["xaxis"] = xaxislistarray;
                    visualobj["yaxis"] = yaxislistarray;
                    var mergedaxislist = xaxislistarray.concat(yaxislistarray);
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
                    dataObj["x"] = $(xaxislist[0]).find("span").text();

                    if(dataObj["x"]==undefined) {
                        var chart = c3.generate({
                            bindto: '#chart',
                            data: dataObj
                        });
                    } else {
                      var chart = c3.generate({
                        bindto: '#chart',
                        data: dataObj,
                        axis: {
                          x: {
                            type: 'category',
                            tick: {
                              rotate: 50,
                              multiline: false
                            }
                         }
                      }
                    });
                  }
                }
                else if(charttype=="pie") {
                    var xaxislistarray = [];
                    var xaxislist = $(".x-axis-dropzone-list li");
                    var len = xaxislist.length;
                    for(var i=0;i<len;i++) {
                        xaxislistarray.push($(xaxislist[i]).find("span").text());
                    }
                    visualobj["xaxis"] = xaxislistarray;
                    var mergedaxislist = xaxislistarray;
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
                    var chart = c3.generate({
                        bindto: '#chart',
                        data: dataObj
                    });
                }
                else if(charttype=="table") {
                    var xaxislistarray = [];
                    var xaxislist = $(".x-axis-dropzone-list li");
                    var len = xaxislist.length;
                    for(var i=0;i<len;i++) {
                        xaxislistarray.push($(xaxislist[i]).find("span").text());
                    }
                    visualobj["xaxis"] = xaxislistarray;
                    var mergedaxislist = xaxislistarray;
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

                    $(".generated-graph").addClass("table");
                    var rowsCount = selectedColumns.length;
                    var columnsCount = selectedColumns[0].length;
                    
                    for(var i=0;i<columnsCount;i++) {
                        if(i==0) {
                            for(var j=0;j<rowsCount;j++) {
                                $("#raw-data-listing-header").append('\
                                    <th>'+selectedColumns[j][0]+'</th>\
                                ');
                            }
                        }
                        else if(i!==0) {
                            var temp = "";
                            for(var j=0;j<rowsCount;j++) {
                                temp = temp+"<td>"+selectedColumns[j][i]+"</td>";
                            }
                            $("#raw-data-listing-body").append('<tr>'+temp+'</tr>');
                        }
                    }
                    var containerInnerHeight = window.innerHeight;
                    $(".view-list-table.visual-table-view").height(containerInnerHeight-460);
                }
            }

            $(document).on("click","#apply-selected-columns-btn",function() {
                $(".blkr").addClass("show");
                $("#container").removeClass("choosecolumns");
                $("#container").addClass("graph");
                render();
                $(".blkr").removeClass("show");
            });
            
            $(document).on("click","#save-visualisation-btn",function() {
                $(".add-item-popup").addClass("show");
                $("#visual-name").focus();
            });

            function dragStopListener (event) {
                $(".draggable").removeClass("drag-active");
            }

            function dragMoveListener (event) {
                $(".draggable").removeClass("drag-active");
                $(event.target).addClass("drag-active");
                var target = event.target,
                    // keep the dragged position in the data-x/data-y attributes
                    x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
                    y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                // translate the element
                target.style.webkitTransform =
                target.style.transform =
                'translate(' + x + 'px, ' + y + 'px)';

                // update the posiion attributes
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
            }

            // enable draggables to be dropped into this
            interact('.dropzone').dropzone({  
            // Require a 50% element overlap for a drop to be possible
            overlap: 0.50,

            // listen for drop related events:

            ondropactivate: function (event) {
                // add active dropzone feedback
                event.target.classList.add('drop-active');
            },
            ondragenter: function (event) {
                var draggableElement = event.relatedTarget,
                    dropzoneElement = event.target;

                // feedback the possibility of a drop
                dropzoneElement.classList.add('drop-target');
            },
            ondragleave: function (event) {
                // remove the drop feedback style
                event.target.classList.remove('drop-target');
                console.log("dropped out side");
            },
            ondrop: function (event) {
                $(event.target).find("ul").append('<li><span>'+event.relatedTarget.textContent+'</span><img class="delete-drop" src="images/delete.svg"></li>');
                event.relatedTarget.remove();
                // console.log("dropped");
            },
            ondropdeactivate: function (event) {
                // remove active dropzone feedback
                event.target.classList.remove('drop-active');
                event.target.classList.remove('drop-target');
                event.relatedTarget.removeAttribute("data-x");
                event.relatedTarget.removeAttribute("data-y");
                event.relatedTarget.removeAttribute("style");
            }
            });

            $("#container").addClass(proc);
            $(document).on("click",".top-nav-items li",function(e) {
                $(".top-nav-items li").removeClass("select");
                $(this).addClass("select");
                e.stopPropagation();
            });

            $(document).on("click","body",function() {
                $(".top-nav-items li").removeClass("select");
            });

            $(document).on("click",".back-btn",function() {
                $(".container").removeClass("tab1 tab2 tab3 tab4 tab5");
                $(".container").addClass("tab5");
            });

            $(document).on("click",".eye-icon",function() {
                var clickedViewId = $(this).closest("tr").data("id");
                window.location.assign("visuals.php?proc=view&visualid="+clickedViewId);
            });

            function empty(e) {
                if(e==undefined) {
                    return "-";
                }
                else {
                    return e;
                }
            }

            if(proc=="list") {
                $(".blkr").addClass("show");
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
                            // <span class="edit-code"><svg viewBox="0 0 512 512"><path d="M460.943,147.656L310.318,5.089C306.859,1.822,302.291,0,297.53,0H63.845C53.574,0,45.249,8.325,45.249,18.596v474.809 c0,10.271,8.325,18.596,18.596,18.596h384.31c10.271,0,18.596-8.325,18.596-18.596V161.162 C466.751,156.055,464.649,151.17,460.943,147.656z M429.559,474.809H82.441V37.191h207.689l139.43,131.973V474.809z"/><path d="M379.332,316.566l-45.249-53.927c-6.595-7.86-18.323-8.901-26.201-2.287c-7.86,6.601-8.889,18.329-2.287,26.201 l35.22,41.97l-35.214,41.97c-6.608,7.872-5.579,19.606,2.287,26.201c3.49,2.926,7.73,4.351,11.945,4.351 c5.306,0,10.581-2.256,14.25-6.645l45.249-53.927C385.141,333.562,385.141,323.477,379.332,316.566z"/><path d="M206.405,370.493l-35.226-41.97l35.22-41.964c6.601-7.872,5.579-19.606-2.293-26.201 c-7.866-6.602-19.594-5.573-26.201,2.287l-45.249,53.927c-5.802,6.911-5.802,16.996,0,23.908l45.249,53.927 c3.682,4.382,8.951,6.639,14.257,6.639c4.221,0,8.461-1.426,11.951-4.351C211.978,390.093,213.007,378.366,206.405,370.493z"/><path d="M272.426,220.259c-10.066-1.531-19.631,5.442-21.174,15.596l-27.274,179.758c-1.543,10.159,5.442,19.637,15.596,21.174 c0.948,0.143,1.884,0.211,2.814,0.211c9.037,0,16.959-6.595,18.36-15.806l27.274-179.758 C289.565,231.28,282.579,221.803,272.426,220.259z"/><path d="M316.126,142.567V18.596h-37.191v142.567c0,10.271,8.325,18.596,18.596,18.596h150.625v-37.191H316.126z"/></svg></span>\
                            $("#visualisation-tbody").append('\
                                <tr data-id="'+body[i].id+'">\
                                    <td>'+empty(body[i].name)+'</td>\
                                    <td>'+empty(body[i].desription)+'</td>\
                                    <td>'+empty(body[i].type)+'</td>\
                                    <td>'+empty(metadataObj.lastmodified)+'</td>\
                                    <td>\
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
            else if((proc=="edit")||(proc="new")) {
                $("#container").addClass("visualselect");
            }
            $(document).on("click",".chart-label",function() {
                $(".blkr").addClass("show");
                $(".global-message-block").removeClass("error-m");
                if(proc=="new") {
                    charttype = $(this).data("chart-type");
                    visualobj["charttype"] = charttype; 
                    $.ajax({
                        method: "GET",
                        url: api.base+"query/"+queryId,
                        headers: { 'authorization': api.auth },
                        processData: false,
                        crossDomain: true,
                        data: {},
                        timeout: global.timeout
                    })
                    .done(function(e) {
                        if(e.success==true) {
                            jsonQuery = e;
                            var querybox = JSON.parse(e.body.content).query;
                            visualobj["querybox"] = querybox;
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
                                    columnnames = e.body[0].columns;
                            
                                    for (a in columnnames ) {
                                        $("#draggable-list").append('\
                                            <li class="draggable">'+columnnames[a]+'</li>\
                                        ');
                                    }
                            
                                    interact('.draggable')  
                                        .draggable({
                                        // enable inertial throwing
                                        inertia: true,
                                        // keep the element within the area of it's parent
                                        restrict: {
                                            restriction: "self",
                                            endOnly: true,
                                            elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
                                        },
                                        // enable autoScroll
                                        autoScroll: true,
                                        // call this function on every dragmove event
                                        onmove: dragMoveListener,
                                        onend: dragStopListener
                                    });
                                    $("#container").removeClass("visualselect");
                                    $("#container").addClass("choosecolumns");
                                    if(charttype=="pie") {
                                        $(".columns-box").addClass("one-axis");
                                        $(".columns-box").removeClass("table");
                                    }
                                    else if(charttype=="table") {
                                        $(".columns-box").addClass("one-axis");
                                        $(".columns-box").addClass("table");
                                    }
                                    else {
                                        $(".columns-box").removeClass("one-axis");
                                    }
                                    $(".dropzone ul").empty();
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
            });
        });
    </script>
</body>
</html>
