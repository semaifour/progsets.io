<?php
    if(isset($_GET["proc"])) {
        $proc =  $_GET["proc"];
        if($proc=="new") {

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
    // var queryId = "<?php if(isset($_GET["queryid"])) { echo $queryId; } else { echo ""; } ?>";
    // var visualId = "<?php if(isset($_GET["visualid"])) { echo $visualId; } else { echo ""; } ?>";

    // if(proc=="edit") {
    //     var editId = "<?php if(isset($_GET["id"])) { echo $editId; } else { echo ""; } ?>";
    // }
</script>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Dashboards">
    <title>PROCSETS - Dashboards</title>
    <link rel="icon" href="">
    <script src="js/jquery.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/c3.css" rel="stylesheet">
    <link href="css/app.css" rel="stylesheet">
    <script src="js/d3.v3.min.js"></script>
    <script src="js/c3.min.js"></script>
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
    <div class="popup-block"></div>
    <!-- end:add item popup -->

    <!-- start:header -->
    <header class="header">
        <div class="dashboard-top-nav">
            <a href="index.php" class="logo-block">
                <span class="logo-icon">
                    <svg viewBox="0 0 158.19 179">
                        <path d="M135.99 124.56c-10.36 12.49-26 20.44-43.49 20.44-10.12 0-19.62-2.66-27.84-7.32-9 3.66-25.33 4.15-25.33 4.15l12.94-13.66C42.21 117.97 36 103.96 36 88.5 36 57.3 61.3 32 92.5 32c17.2 0 32.61 7.69 42.98 19.83l22.31-20.18C141.38 12.29 116.87 0 89.5 0 40.07 0 0 40.07 0 89.5S40.07 179 89.5 179c27.6 0 52.28-12.49 68.69-32.13l-22.2-22.31z"></path><path d="M115.875 72.25a3.375 3.375 0 0 1-3.375 3.375H69.375a3.375 3.375 0 1 1 0-6.75H112.5a3.375 3.375 0 0 1 3.375 3.375zM115.875 90a3.125 3.125 0 0 1-3.125 3.125H69.125a3.125 3.125 0 1 1 0-6.25h43.625A3.125 3.125 0 0 1 115.875 90zM90.938 107.438a3.563 3.563 0 0 1-3.563 3.563H69.563a3.563 3.563 0 1 1 0-7.126h17.813a3.563 3.563 0 0 1 3.562 3.563z"></path>
                    </svg>
                </span>
                <div class="logo-text">PROCSETS</div>
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

    <!-- start:screens -->
    <div id="container" class="container dashboards-container">
        <div style="margin-top: 0;" class="select-visual-type">
            <div class="container-fluid">
            <div class="save-right">
                <button id="save-dashboard-btn" class="small primary-btn">Save</button>
            </div>
                <!-- <h1>Serialization demo</h1> -->

                <!-- <div>
                    <a class="btn btn-default" id="save-grid" href="#">Save Grid</a>
                    <a class="btn btn-default" id="load-grid" href="#">Load Grid</a>
                    <a class="btn btn-default" id="clear-grid" href="#">Clear Grid</a>
                </div> -->

                <!-- <br/> -->

                <div class="grid-stack">
                </div>

                <!-- <textarea id="saved-data" cols="100" rows="20" readonly="readonly"></textarea> -->
            </div>
        </div>
        <!-- start:saved visualisation list -->
        <section class="grid-list">
            <h4>Visuals List</h4>
            <table class="view-list-table">
                <thead>
                    <tr>
                        <th>Dashboard Name</th>
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
    </div>
    <!-- end:screens -->

    <script type="text/javascript">
        $(function () {
            var chartcount = 1;
            var charts = [];
            $('.grid-stack').on('gsresizestop', function(event, elem) {
                charts[$(elem).find(".graph").attr("id")].resize();
            });
            $(document).on("click",".add-chart-btn",function() {
                $(this).addClass("hide");
                $(this).closest(".add-chart-btn-wrap").siblings(".grid-stack-item-content").append('<div class="graph" id="chart'+chartcount+'"></div>');
                var charttype = "line";
                // $("#chart-name-title").text($(this).closest("tr").find(".chartname").text());
                var columndata;
                $.ajax({method: "GET", url: "/api/v1/demo", headers: {}, data: {} }) .done(function(e) {
                    e = JSON.parse(e);
                    columndata = e.body["result-verticals"];
                    bindtochart = '#chart'+chartcount;
                    charts["chart"+chartcount] = c3.generate({
                        padding: {
                            top: 10,
                            bottom: 10,
                        },
                        bindto: bindtochart,
                        data: {
                          columns: columndata,
                          type: charttype
                        }
                    });
                    chartcount = chartcount+1;
                });
            });

            $(document).on("click",".close-popup",function() {
                $(".add-item-popup").removeClass("show");
            });
            
            $(document).on("click","#save-dashboard-btn",function() {
                $(".add-item-popup").addClass("show");
                $("#dashboard-name").focus();
            });
            
            var options = {};
            $('.grid-stack').gridstack(options);

            new function () {
                this.serializedData = [
                    {
                        x: 0,
                        y: 0,
                        width: 8,
                        height: 4
                    },
                    {
                        x: 8,
                        y: 0,
                        width: 4,
                        height: 4
                    },
                    {
                        x: 8,
                        y: 7,
                        width: 4,
                        height: 2
                    },
                    {
                        x: 0,
                        y: 4,
                        width: 4,
                        height: 3
                    },
                    {
                        x: 8,
                        y: 4,
                        width: 4,
                        height: 3
                    },
                    {
                        x: 4,
                        y: 4,
                        width: 4,
                        height: 3
                    },
                    {
                        x: 0,
                        y: 7,
                        width: 4,
                        height: 2
                    },
                    {
                        x: 4,
                        y: 7,
                        width: 4,
                        height: 2
                    }
                ];

                this.grid = $('.grid-stack').data('gridstack');

                this.loadGrid = function () {
                    this.grid.removeAll();
                    var items = GridStackUI.Utils.sort(this.serializedData);
                    _.each(items, function (node) {
                        this.grid.addWidget($('<div><div class="grid-stack-item-content" /><div class="add-chart-btn-wrap"><button class="add-chart-btn small secondary-btn">Add chart</button></div><div class="delete-widget"><span class="delete-widget-icon"><svg viewBox="0 0 41.756 41.756"><path d="M27.948,20.878L40.291,8.536c1.953-1.953,1.953-5.119,0-7.071c-1.951-1.952-5.119-1.952-7.07,0L20.878,13.809L8.535,1.465 c-1.951-1.952-5.119-1.952-7.07,0c-1.953,1.953-1.953,5.119,0,7.071l12.342,12.342L1.465,33.22c-1.953,1.953-1.953,5.119,0,7.071 C2.44,41.268,3.721,41.755,5,41.755c1.278,0,2.56-0.487,3.535-1.464l12.343-12.342l12.343,12.343 c0.976,0.977,2.256,1.464,3.535,1.464s2.56-0.487,3.535-1.464c1.953-1.953,1.953-5.119,0-7.071L27.948,20.878z"/> </svg></span></div><div/>'),
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
                        return {
                            x: node.x,
                            y: node.y,
                            width: node.width,
                            height: node.height
                        };
                    }, this);
                    var visualobj = {};
                    var querycol = [];
                    visualobj["griddata"] = this.serializedData;
                    visualobj["dashboardidentifier"] = $("#dashboard-name").val();
                    visualobj["dashboarddescription"] = $("#dashboard-description").val();
                    querycol.push(visualobj);
                    $.ajax({
                        method: "POST",
                        url: "/api/v1/queries.php",
                        headers: {},
                        data: {
                            queryout:3,
                            querycolumns:JSON.stringify(querycol)
                        }
                    })
                    .done(function(e) {
                         $(".add-item-popup").removeClass("show");
                    });
                    // $('#saved-data').val(JSON.stringify(this.serializedData, null, '    '));
                    return false;
                }.bind(this);

                this.clearGrid = function () {
                    this.grid.removeAll();
                    return false;
                }.bind(this);

                $('#save-grid').click(this.saveGrid);
                $('#load-grid').click(this.loadGrid);
                $('#clear-grid').click(this.clearGrid);

                this.loadGrid();
                $('.delete-widget').click(this.deleteWidget);
            };
        });
    </script>
</body>
</html>