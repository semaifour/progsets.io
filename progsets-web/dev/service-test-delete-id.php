<?php
    if(isset($_GET["id"])) {
        $deleteId = $_GET["id"];
    }
?>
<script>
    var deleteId = "<?php echo $deleteId; ?>";
</script>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="page" content="Dashboards">
    <title></title>
    <link rel="icon" href="">
    <script src="../js/jquery.min.js"></script>
    </head>
  <body>
  <script type="text/javascript">
        $(function () {
            var encodedString2 = "Basic e3ea861571c789c088722da097a50d14414a3c8ccc34f842:93d2cbd59c74a848e158ab7b2d10ab2eb76e084d7ccabd09";
            
            $.ajax({
                method: "DELETE",
                url: "http://procsets.semaifour.com:8175/procsets/rest/psql/entity/visual/"+deleteId,
                headers: {
                    'authorization': encodedString2
                },
                processData: false,
                crossDomain: true,
                data: {}
            })
            .done(function(e) {
                console.log(e);
            });
        });

        // $.ajax({
        //     method: "POST",
        //     url: "http://procsets.semaifour.com:8175/procsets/rest/psql/exe?return.as=verticals",
        //     headers: {'authorization': encodedString,},
        //     contentType: 'text/psql',
        //     processData: false,
        //     data: querybox,
        //     crossDomain: true
        // })
        // .done(function(e) {
        // });
  </script>
</body>
</html>