<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link rel="icon" href="">
    <script src="/demo/js/jquery-2.1.1.js"></script>
    <link href="/demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/demo/css/style.css" rel="stylesheet">
    <link href="/demo/css/c3.css" rel="stylesheet">
    <script src="/demo/js/d3.v3.min.js"></script>
    <script src="/demo/js/c3.min.js"></script>
    <style>
        .form-group {
            margin-bottom: 20px;
        }
        body {
            background-color: #fff;
        }
        .block {
            margin-bottom: 60px;
            margin-top: 30px;
        }
        .form-label {
            margin-bottom: 5px;
        }
        .form-group input {
            min-width: 300px;
            height: 35px;
            padding-left: 10px;
        }
    </style>
  </head>
  <body>
    <div class="container">
        <fieldset>
            <h4>Generated graph</h4>
            <div id="chart"></div>
        </fieldset>
    </div>
</body>
<script>
    $(document).ready(function () {
        $.ajax({
            method: "GET",
            url: "/api/v1/demo",
            headers: {},
            data: {}
        })
        .done(function(e) {
            e = JSON.parse(e);
            var chart = c3.generate({
                bindto: '#chart',
                data: {
                    columns: e.body["result-verticals"],
                    type: 'bar'
                },
                bar: {
                    width: {
                        ratio: 0.5
                    }
                }
            });
        });

        
    });
</script>
</html>