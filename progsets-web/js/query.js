$(document).ready(function () {
    var editBody;
    var nextstep = "no";
    var deleteitem;
    
    $(document).on("click",".edit-icon",function() {
        var clickedEditId = $(this).closest("tr").data("id");
        window.location.assign("queries.php?proc=edit&id="+clickedEditId);
    });

    $("#container").addClass(proc);
    if(proc=="list") {
        $(".blkr").addClass("show");
        $(".global-message-block").removeClass("error-m");
        $.ajax({
            method: "GET",
            url: api.base+"query",
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
                    $("#queries-tbody").append('\
                        <tr data-id="'+body[i].id+'">\
                            <td>'+empty(body[i].name)+'</td>\
                            <td>'+empty(body[i].desription)+'</td>\
                            <td>'+empty(body[i].type)+'</td>\
                            <td>'+empty(metadataObj.lastmodified)+'</td>\
                            <td>\
                                <span class="edit-icon"><svg viewBox="0 0 528.899 528.899"><path d="M328.883,89.125l107.59,107.589l-272.34,272.34L56.604,361.465L328.883,89.125z M518.113,63.177l-47.981-47.981c-18.543-18.543-48.653-18.543-67.259,0l-45.961,45.961l107.59,107.59l53.611-53.611C532.495,100.753,532.495,77.559,518.113,63.177z M0.3,512.69c-1.958,8.812,5.998,16.708,14.811,14.565l119.891-29.069L27.473,390.597L0.3,512.69z"/></svg></span>\
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
    else if(proc=="edit") {
        $(".blkr").addClass("show");
        $(".global-message-block").removeClass("error-m");
        $.ajax({
            method: "GET",
            url: api.base+"query/"+editId,
            headers: { 'authorization': api.auth },
            processData: false,
            crossDomain: true,
            data: {},
            timeout: global.timeout
        })
        .done(function(e) {
            if(e.success==true) {
                var body = e.body;
                editBody = e.body;
                var metadataObj = JSON.parse(body.content);
                $(".query-box").val(metadataObj.query);
                $("#query-name").val(metadataObj.queryidentifier);
                $("#query-description").val(metadataObj.querydescription);
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

    $(document).on("click",".top-nav-items li",function(e) {
        $(".top-nav-items li").removeClass("select");
        $(this).addClass("select");
        e.stopPropagation();
    });

    $(document).on("click","body",function() {
        $(".top-nav-items li").removeClass("select");
    });
    
    function empty(e) {
        if(e==undefined) {
            return "-";
        }
        else {
            return e;
        }
    }

    $(document).on("click","#execute-query-btn",function() {
        $(".blkr").addClass("show");
        $(".global-message-block").removeClass("error-m");
        var querybox = $(".query-box").val().replace(/(?:\r\n|\r|\n)/g, '\n');
        $.ajax({
            method: "POST",
            url: api.query5,
            headers: {'authorization': api.auth},
            contentType: 'text/psql',
            processData: false,
            data: querybox,
            crossDomain: true,
            timeout: global.timeout
        })
        .done(function(e) {
            if(e.success==true) {
                $("#raw-data-listing-header").empty();
                $("#raw-data-listing-body").empty();
                columndata = e.body[0].data;
                columnnames = e.body[0].columns;
        
                for (a in columnnames ) {
                    $("#raw-data-listing-header").append('\
                        <th>'+columnnames[a]+'</th>\
                    ');
                }
        
                var rowsCount = columndata.length;
                var columnsCount = columndata[0].length;
                
                for(var i=0;i<columnsCount;i++) {
                    if(i!==0) {
                        var temp = "";
                        for(var j=0;j<rowsCount;j++) {
                            temp = temp+"<td>"+columndata[j][i]+"</td>";
                        }
                        $("#raw-data-listing-body").append('<tr>'+temp+'</tr>');
                    }
                }
                
                if(proc=="edit") {
                    $(".query-container").removeClass("list edit");
                    $(".query-container").addClass("edit save-query");
                    // set height for raw-data-wrap
                    var containerInnerHeight = window.innerHeight;
                    $(".raw-data-wrap").height(containerInnerHeight-400);
                }
                else if(proc=="new") {
                    $(".query-container").removeClass("list new");
                    $(".query-container").addClass("new save-query");
                    // set height for raw-data-wrap
                    var containerInnerHeight = window.innerHeight;
                    $(".raw-data-wrap").height(containerInnerHeight-400);
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
    });

    // save query button is clicked and need to show pop form to enter name and description
    $(document).on("click","#save-query-btn",function() {
        $(".add-item-popup").addClass("show");
        $("#query-name").focus();
    });

    // create visual button is clicked
    $(document).on("click","#create-visual-btn",function() {
        nextstep = "yes";
        if(proc=="edit") {
            $("#update-query-btn").trigger("click");
        }
        else if(proc=="new") {
            $(".add-item-popup").addClass("show");
            $("#query-name").focus();
        }
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
        if(proc=="edit") {
            $(".query-container").removeClass("list edit save-query");
            $(".query-container").addClass("edit");
        }
        else if(proc=="new") {
            $(".query-container").removeClass("new save-query");
            $(".query-container").addClass("new");
        }
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
            url: api.base+"query/"+deleteitem,
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
                window.location.assign("queries.php?proc=list");
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

    // update query button is clicked, call query update service
    $(document).on("click","#update-query-btn",function() {
        $(".blkr").addClass("show");
        $(".global-message-block").removeClass("error-m");
        var visualobj = {};
        visualobj["queryidentifier"] = $("#query-name").val();
        visualobj["querydescription"] = $("#query-description").val();
        visualobj["query"] = $(".query-box").val().replace(/(?:\r\n|\r|\n)/g, '\n');
        visualobj["lastmodified"] = js_yyyy_mm_dd_hh_mm_ss ();
        editBody.content = JSON.stringify(visualobj);
        $.ajax({
            method: "PUT",
            url: api.base+"query/"+editId,
            headers: {'authorization': api.auth},
            contentType: 'application/json',
            processData: false,
            crossDomain: true,
            data: JSON.stringify(editBody),
            timeout: global.timeout
        })
        .done(function(e) {
            if(e.success==true) {
                if(nextstep=="yes") {
                    window.location.assign("visuals.php?proc=new&queryid="+e.body.id);
                }
                else if(nextstep=="no") {
                    window.location.assign("queries.php?proc=list");
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
    });

    // save query button is clicked in the popup, call query save service
    $(document).on("click","#pop-up-save-query-btn",function() {
        $(".blkr").addClass("show");
        $(".global-message-block").removeClass("error-m");
        var visualobj = {};
        visualobj["queryidentifier"] = $("#query-name").val();
        visualobj["querydescription"] = $("#query-description").val();
        visualobj["query"] = $(".query-box").val().replace(/(?:\r\n|\r|\n)/g, '\n');
        visualobj["lastmodified"] = js_yyyy_mm_dd_hh_mm_ss ();
        $.ajax({
            method: "POST",
            url: api.base+"query?name="+visualobj["queryidentifier"],
            headers: {'authorization': api.auth},
            contentType: 'text/plain',
            processData: false,
            crossDomain: true,
            data: JSON.stringify(visualobj),
            timeout: global.timeout
        })
        .done(function(e) {
            if(e.success==true) {
                if(nextstep=="yes") {
                    window.location.assign("visuals.php?proc=new&queryid="+e.body.id);
                }
                else if(nextstep="no") {
                    window.location.assign("queries.php?proc=list");
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
    });

    $(document).on("click",".close-popup",function() {
        $(".add-item-popup").removeClass("show");
        $(".delete-item-popup").removeClass("show");
    });
});