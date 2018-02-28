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
            var visualobj = {};
            visualobj["queryidentifier"] = "test name";
            visualobj["querydescription"] = "test description";
            visualobj["query"] = "query here";
            visualobj["lastmodified"] = "time stamp";
            var encodedString2 = "Basic e3ea861571c789c088722da097a50d14414a3c8ccc34f842:93d2cbd59c74a848e158ab7b2d10ab2eb76e084d7ccabd09";
            $.ajax({
                method: "PUT",
                url: "http://procsets.semaifour.com:8175/procsets/rest/psql/entity/query/5a543d5037faec31fd8da7b5",
                headers: {
                    'authorization': encodedString2
                },
                processData: false,
                crossDomain: true,
                contentType: 'application/json',
                data: JSON.stringify(visualobj)
            })
            .done(function(e) {
                console.log(e);
            });
        });
  </script>
</body>
</html>