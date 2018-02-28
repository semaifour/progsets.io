<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Welcome">
    <title>Progsets : sparking data intelligence </title>
    <link href="images/favicon.webp" type="image/webp" rel="icon">
    <script src="js/jquery.min.js"></script>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link href="css/c3.css" rel="stylesheet">
    <link href="css/app.css" rel="stylesheet">
    <script src="js/d3.v3.min.js"></script>
    <script src="js/c3.min.js"></script>
    <script src="js/interact.min.js"></script>
    <script src="js/config.js"></script>
    <script src="js/nav.js"></script>
  </head>
  <body>
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
                    <li>
                        <div class="email-profile">demo</div>
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
            <div class="sub-title"></div>
            <h4 style="margin-bottom: 70px;font-size: 32px;">Welcome! </h4>
            <div class="chart-tabs-block">
                <!-- chart tab start-->
                <div id="step2" class="chart-tab">
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
            <div style="text-align: center; padding-top: 70px;">
                <div style="text-align: center; margin-bottom: 20px;" class="sub-title">Let us spark business data sources</div>
                <a href="queries.php?proc=list">
                    <button style="width: 140px;" class="primary-btn">Spark IT </button>
                </a>
            </div>
        </div>
        <!-- chart tabs section end-->
    </div>
</body>
</html>
