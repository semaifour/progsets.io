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
    }
?>
<script>
    var proc = "<?php if(isset($_GET["proc"])) { echo $proc; } else { echo ""; } ?>";
    if(proc=="edit") {
        var editId = "<?php if(isset($_GET["id"])) { echo $editId; } else { echo ""; } ?>";
    }
    
</script>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Queries">
    <title>Progsets - Queries</title>
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
    <script src="js/query.js"></script>
    <script src="js/nav.js"></script>
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
        <h4 class="popup-add-item-title">Save query</h4>
        <div class="close-popup">
            <span class="close-icon">
                <svg>
                    <svg id="close" viewBox="0 0 14.72814.728" width="100%" height="100%"><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435.293a1 1 0 0 1 0 1.415L1.707 14.435a.999.999 0 1 1-1.414-1.414L13.021.293a.999.999 0 0 1 1.414 0z"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M14.435 14.435a.999.999 0 0 1-1.414 0L.293 1.708A1 1 0 1 1 1.707.293l12.728 12.729a.998.998 0 0 1 0 1.413z"></path></svg>
                </svg> 
            </span>
        </div>
        <div class="form-group">
            <div class="form-label">Query name</div>
            <!-- <input type="text" id="chart-name" class="text-input"> -->
            <input type="text" id="query-name" class="text-input">
        </div>
        <div class="form-group">
            <div class="form-label">Description</div>
            <!-- <textarea id="chart-description" class="textarea-input"></textarea> -->
            <textarea id="query-description" class="textarea-input"></textarea>
        </div>
        <div class="right">
            <!-- <button id="add-graph-btn" class="small primary-btn">Save</button> -->
            <button id="pop-up-save-query-btn" class="small primary-btn">Save</button>
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
                <div class="logo-text">PROCSETS</div> -->
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
    <div id="container" class="container query-container">
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
  
        <!-- start:extract data -->
        <section class="extract-query-section">
            <div class="query-description">Interactively enter, edit, and execute queries.</div>
            <h4>Extract data</h4>
            <div class="query-box-wrap">
                <textarea class="query-box" cols="30" rows="10"></textarea>
                <button id="execute-query-btn" class="primary-btn">EXECUTE</button>
            </div>
        </section>
        <!-- end:extract data -->

        <!-- start:choose columns -->
        <section class="save-query-section">
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
            <div class="right">
                <button id="cancel-query-save-btn" class="line-btn">Cancel</button>
                <button id="save-query-btn" class="save-query-last primary-btn">Save Query</button>
                <button id="update-query-btn" class="update-query-last primary-btn">Update Query</button>
                <button id="create-visual-btn" class="primary-btn">Create Visual</button>
            </div>
            <!-- end:raw data listing -->
        </section>
        <!-- end:extract data -->

        <!-- start:saved queries list -->
        <section class="grid-list">
            <h4>Queries<a class="new-query" href="queries.php?proc=new"> [New Query] </a></h4>
            <table class="view-list-table">
                <thead>
                    <tr>
                        <th>Query Name</th>
                        <th>Description</th>
                        <th>Query</th>
                        <th>Last Modified</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="queries-tbody"></tbody>
            </table>
        </section>
        <!-- end:saved queries list -->

    </div>
    <!-- end:screens -->
</body>
</html>
