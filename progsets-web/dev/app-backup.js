$(document).ready(function () {
    $("#container").addClass(proc);
    
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
    
    // set height for raw-data-wrap
    var containerInnerHeight = window.innerHeight;
    $(".raw-data-wrap").height(containerInnerHeight-400);
    
    $(document).on("click",".delete-drop",function() {
        var eleValue = $(this).closest("li").find("span").text();
        $(this).closest("li").remove();
        $("#draggable-list").append('<li class="draggable">'+eleValue+'</li>');
    });

    $(document).on("click",".top-nav-items li",function(e) {
        $(".top-nav-items li").removeClass("select");
        $(this).addClass("select");
        e.stopPropagation();
    });

    $(document).on("click","body",function() {
        $(".top-nav-items li").removeClass("select");
    });
    
    var columndata;
    // step 1 select type of chart (action element - chart-label)
    // step 2 user enters query (action element - execute-query-btn)
    // step 3 user drags columns to X and Y axis (action element - apply-selected-columns-btn)
    // step 4 user show generated chart (action element - save-graph-btn)

    var charttype;
    function render(chartype) {
        // var col1 = $("#col1").val();
        // var col2 = $("#col2").val();
        // var col1 = "30, 200, 100, 400, 150, 250";
        // var col2 = "50, 20, 10, 40, 15, 25";
        // var columndata = [];
        
        // col1 = col1.split(",");
        // for (a in col1 ) {
        //     col1[a] = parseInt(col1[a], 10);
        // }
        // col1.unshift("Data 1");
        // columndata.push(col1);

        // col2 = col2.split(",");
        // for (a in col2 ) {
        //     col2[a] = parseInt(col2[a], 10);
        // }
        // col2.unshift("Data 2");
        // columndata.push(col2);

        var chart = c3.generate({
            bindto: '#chart',
            data: {
              columns: columndata,
              type: charttype
            }
        });
    }

    // $(document).on("click","#submit-data",function() {
    //     render();
    // });

    function empty(e) {
        if(e==undefined) {
            return "-";
        }
        else {
            return e;
        }
    }
    $(document).on("click",".chart-label",function() {
        $(".container").removeClass("tab1 tab2 tab3 tab4");
        $(".container").addClass("tab2");
        charttype = $(this).data("chart-type");
        var html = $(".query-box").val();
        $(".query-box").focus().val("").val(html);
        $("#step1").addClass("success");
        $("#step1").removeClass("active");
        $("#step2").addClass("active");
        // $(".query-box").focus();
    });

    // var tablecolumns;
    // var apiurl = "services/columns";
    // $.ajax({
    //     method: "GET",
    //     url: apiurl,
    //     headers: {},
    //     data: {}
    // })
    // .done(function(e) {
    //     e = JSON.parse(e);
    //     tablecolumns = e.body["result"];
    //     var columnslist = Object.keys(tablecolumns[0]);
    // });

    $(document).on("click","#execute-query-btn",function() {
        $(".blkr").addClass("show");
        var querybox = $(".query-box").val().replace(/(?:\r\n|\r|\n)/g, '\n');
        $.ajax({
            method: "POST",
            url: api.query5,
            headers: {'authorization': api.auth},
            contentType: 'text/psql',
            processData: false,
            data: querybox,
            crossDomain: true
        })
        .done(function(e) {
            // e = JSON.parse(e);
            // columndata = e.body["data"];
            columndata = e.body[0].data;
            columnnames = e.body[0].columns;
    
            for (a in columnnames ) {
                $("#draggable-list").append('\
                    <li class="draggable">'+columnnames[a]+'</li>\
                ');
                $("#raw-data-listing-header").append('\
                    <th>'+columnnames[a]+'</th>\
                ');
            }
    
            var rowsCount = columndata.length;
            var columnsCount = columndata[0].length;
            // var rowData = [];
            
            for(var i=0;i<columnsCount;i++) {
                if(i!==0) {
                    var temp = "";
                    for(var j=0;j<rowsCount;j++) {
                        temp = temp+"<td>"+columndata[j][i]+"</td>";
                    }
                    $("#raw-data-listing-body").append('<tr>'+temp+'</tr>');
                }
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
            // var chart = c3.generate({
            //     bindto: '#chart',
            //     data: {
            //         columns: e.body["result-verticals"],
            //         type: 'bar'
            //     },
            //     bar: {
            //         width: {
            //             ratio: 0.5
            //         }
            //     }
            // });
            $(".container").removeClass("tab1 tab2 tab3 tab4");
            $(".container").addClass("tab3");
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
        });
        
        // $("#step2").addClass("success");
        // $("#step2").removeClass("active");
        // $("#step3").addClass("active");
    });

    $(document).on("click","#apply-selected-columns-btn",function() {
        $(".container").removeClass("tab1 tab2 tab3 tab4");
        $(".container").addClass("tab4");
        render();
        $("#step3").addClass("success");
        $("#step3").removeClass("active");
        $("#step4").addClass("active");
    });

    $(document).on("click",".back-btn",function() {
        $(".container").removeClass("tab1 tab2 tab3 tab4");
        $(".container").addClass("tab1");
        $("#step1").removeClass("success");
        $("#step2").removeClass("success");
        $("#step3").removeClass("success");
        $("#step4").removeClass("active");
        $("#step1").addClass("active");
    });

    $(document).on("click","#save-graph-btn",function() {
        $(".add-item-popup").addClass("show");
        $("#chart-name").focus();
    });

    // save query button is clicked and need to show pop form to enter name and description
    $(document).on("click","#save-query-btn",function() {
        $(".add-item-popup").addClass("show");
        $("#query-name").focus();
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
    
    // cancel is clicked in save query screen and user needs to be taken to screen for re-entering the query.
    $(document).on("click","#cancel-query-save-btn",function() {
        $(".container").removeClass("tab1 tab2 tab3 tab4");
        $(".container").addClass("tab2");
    });

    // save query button is clicked in the popup, call query save service
    $(document).on("click","#pop-up-save-query-btn",function() {
        $(".blkr").addClass("show");
        var visualobj = {};
        var querycol = [];
        visualobj["queryidentifier"] = $("#query-name").val();
        visualobj["querydescription"] = $("#query-description").val();
        visualobj["query"] = $(".query-box").val().replace(/(?:\r\n|\r|\n)/g, '\n');
        visualobj["lastmodified"] = js_yyyy_mm_dd_hh_mm_ss ();
        querycol.push(visualobj)
        $.ajax({
            method: "POST",
            url: api.base+"query?name="+visualobj["queryidentifier"],
            headers: {'authorization': api.auth},
            contentType: 'text/plain',
            processData: false,
            crossDomain: true,
            data: JSON.stringify(querycol)
        })
        .done(function(e) {
            $(".add-item-popup").removeClass("show");
            $(".container").removeClass("tab1 tab2 tab3 tab4");
            $(".container").addClass("tab2");
            $(".blkr").removeClass("show");
        });
    });

    $(document).on("click","#add-graph-btn",function() {
        var visualobj = {};
        var querycol = [];
        visualobj["charttype"] = charttype;
        visualobj["apiurl"] = "services/queries.php";
        visualobj["chartidentifier"] = $("#chart-name").val();
        visualobj["chartdescription"] = $("#chart-description").val();
        visualobj["query"] = "testquery";
        visualobj["lastmodified"] = js_yyyy_mm_dd_hh_mm_ss ();
        querycol.push(visualobj)
        $.ajax({
            method: "POST",
            url: api.base+"visual?name="+visualobj["chartidentifier"],
            headers: {'authorization': api.auth},
            contentType: 'text/plain',
            processData: false,
            crossDomain: true,
            data: JSON.stringify(querycol)
        })
        .done(function(e) {
            $(".add-item-popup").removeClass("show");
            $(".container").removeClass("tab1 tab2 tab3 tab4");
            $(".container").addClass("tab1");
        });
    });

    $(document).on("click",".close-popup",function() {
        $(".add-item-popup").removeClass("show");
    });
});