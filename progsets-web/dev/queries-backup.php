<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Queries">
    <title>Analytics - Queries</title>
    <link rel="icon" href="">
    <script src="js/jquery.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/c3.css" rel="stylesheet">
    <link href="css/app.css" rel="stylesheet">
    <script src="js/d3.v3.min.js"></script>
    <script src="js/c3.min.js"></script>
    <script src="js/interact.min.js"></script>
    <script src="js/config.js"></script>
    <script src="js/app.js"></script>
    <script src="js/nav.js"></script>
  </head>
  <body>
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
            <div class="form-label">Chart name</div>
            <input type="text" id="chart-name" class="text-input">
        </div>
        <div class="form-group">
            <div class="form-label">Description</div>
            <textarea id="chart-description" class="textarea-input"></textarea>
        </div>
        <div class="right">
            <button id="add-graph-btn" class="small primary-btn">Add</button>
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
                <div class="logo-text">ANALYTICS</div>
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
    <div class="container tab1">
        <!-- chart tabs section start-->
        <div class="chart-tabs-section restrict-width">
            <div class="chart-tabs-block">
                <!-- chart tab start-->
                <div id="step2" class="chart-tab active">
                    <img class="chart-tab-icon" src="images/question2.svg" alt="chart-tab-icon" />
                    <div class="chart-tab-text">Query</div>
                </div>
                <div id="step1" class="chart-tab">
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
    
        <!-- start:extract data -->
        <section class="extract-query-section">
            <div class="query-description">Interactively enter, edit, and execute queries.</div>
            <h4>Extract data</h4>
            <div class="query-box-wrap">
                <textarea class="query-box" cols="30" rows="10">finder = ielastic?index=finder_148/message
finder = ssql?sql=select message from finder where opcode='current-location-update' limit 1
tags = jsonify?view=finder&column=message
tags = ssql?sql=select uid, spid, taglist.uid as tuid, taglist.range, taglist.accuracy, taglist.coordinate.latitude, taglist.coordinate.longitude,recvlist.uid as ruid, recvlist.rssi, recvlist.distance from tags lateral view explode(tags.tag_list) as taglist lateral view explode(taglist.receiver_list) as recvlist
result = ssql?sql=select * from tags
return?view=result&as=maps&debug=true
close</textarea>
                <button id="execute-query-btn" class="primary-btn">EXECUTE</button>
            </div>
        </section>
        <!-- end:extract data -->

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
            <!-- start:raw data lisiting -->
            <section class="raw-data-listing">
                <h4>Raw data view</h4>
                <div class="raw-data-wrap">
                    <table class="view-list-table">
                        <thead>
                            <tr id="raw-data-listing-header"></tr>
                        </thead>
                        <tbody id="raw-data-listing-body"></tbody>
                    </table>
                </div>
            </section>
            <!-- end:raw data listing -->
        </section>
        <!-- end:extract data -->

        <!-- start:generated graph -->
        <section class="generated-graph">
            <img class="back-btn" src="images/left-arrow.png" alt="">
            <div style="margin-left: 70px;" class="query-description">Preview graph with applied selections</div>
            <h4 style="margin-left: 70px;">Generated graph</h4>
            <div class="graph" id="chart"></div>
            <div class="right">
                <button id="save-graph-btn" class="primary-btn">Save</button>
            </div>
        </section>
        <!-- end:generated graph -->

        <!-- start:saved visualisation list -->
        <section class="grid-list">
            <h4>Saved Chart List</h4>
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
                <!--
                <div class="analytics-pagination">
                    <span class="pagination-left-icon">
                        <svg>
                            <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#pagination-left"></use>
                        </svg>
                    </span>
                    <div class="choose-week">
                        <div class="week-item">1</div>
                        <div class="week-item">2</div>
                        <div class="week-item active">3</div>
                        <div class="week-item">4</div>
                    </div>
                    <span class="pagination-right-icon">
                        <svg>
                            <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#pagination-right"></use>
                        </svg>
                    </span>
                </div>
                -->
            </table>
        </section>
        <!-- end:saved visualisation list -->

    </div>
    <!-- end:screens -->
</body>
</html>