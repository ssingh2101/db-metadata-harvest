$(document).ready(function () {
    var connData = '';
    var connId = '';
    var connName = '';
    var tableData = '';

    $.getJSON("http://localhost:8080/MavenWebApp/api/v0/databases/", function (data) {

        $.each(data.Connections, function (key, value) {
            connData += '<tr>';
            connData += '<td>' + value.dbType + '</td>';
            connData += '<td>' + value.instance + '</td>';
            connData += '<td>' + value.connName + '</td>';
            connData += '<td>' + value.host + '</td>';
            connData += '<td>' + value.port + '</td>';
            connData += '</tr>';
        });

        $('#dbList').append(connData);


        var name = '';
        $.each(data.Connections, function (key, value) {

            name += '<tr>';
            name += '<td>' + value.connName + '</td>';
            name += '</tr>';

            connId += value.connectionId;
            connName += value.connName;

        });
        $('#sideTable').append(name);


        /*$('#sideTable').click(function () {
         alert(this);
         })*/

        var nameObj = '';
        var currConnId = '';
        $(function () {
            $("#sideTable").find("td").each(function (index) {
                $(this).click(function () {

                    $("#dbTablesArea").html("");
                    $("#schemaTableArea").html("");

                    nameObj = ($(this).text());

                    $.each(data.Connections, function (key, value) {
                        if (nameObj == value.connName) {
                            currConnId = (value.connectionId);
                            console.log(currConnId);
                        }
                    });

                    //Crate table html tag
                    var table = $("<table id=table2 border=1></table>").appendTo("#schemaTableArea");

                    //Create table header row
                    var rowHeader = $("<tr></tr>").appendTo(table);
                    $("<th></th>").text("Schema Name").appendTo(rowHeader);
                    $("<th></th>").text("Schema Id").appendTo(rowHeader);

                    var schemaData = '';
                    $.getJSON("http://localhost:8080/MavenWebApp/api/v0/databases/" + currConnId + "/schemas", function (data1) {

                        $.each(data1.Schemas, function (key, value) {

                            schemaData += '<tr>';
                            schemaData += '<td>' + value.Label + '</td>';
                            schemaData += '<td>' + value.Id + '</td>';
                            schemaData += '</tr>';
                        });


                        $('#table3').hide();
                        $('#dbList').hide();
                        $('#table2').append(schemaData).fadeIn("slow");

                        var nameObj = '';
                        $(function () {
                            $("#table2").find("td").each(function (index) {
                                $(this).click(function () {


                                    //$("#table3").empty();

                                    nameObj = ($(this).text());
                                    var schemaId = '';
                                    var schemaName = '';
                                    $.each(data1.Schemas, function (key, value) {
                                        if (nameObj == value.Label) {
                                            schemaId = (value.Id);
                                            schemaName = (value.Label);
                                        }
                                    });

                                    //Crate table html tag
                                    var table = $("<table id=table3 border=1></table>").appendTo("#dbTablesArea");


                                    //Create table header row
                                    var rowHeader = $("<tr></tr>").appendTo(table);
                                    $("<th></th>").text("Table Name").appendTo(rowHeader);
                                    $("<th></th>").text("Table Id").appendTo(rowHeader);

                                    $.getJSON("http://localhost:8080/MavenWebApp/api/v0/databases/" + currConnId + "/schemas/" + schemaId + "/tables/", function (data2) {
                                        $.each(data2.Tables, function (key, value) {
                                            tableData += '<tr>';
                                            tableData += '<td>' + value.tableName + '</td>';
                                            tableData += '<td>' + value.Id + '</td>';
                                            tableData += '</tr>'
                                        });
                                        $('#dbList').hide();
                                        $('#table2').hide();
                                        $('#table3').append(tableData).fadeIn("slow");

                                    });
                                });
                            });
                        });
                    });


                });
            });
        });


    });

// Dropdown Menu
    var dbTypes = ['Derby', 'Oracle', 'MySql', 'PSQL'];
    var option = '';
    for (var i = 0; i < dbTypes.length; i++) {
        option += '<option value="' + dbTypes[i] + '">' + dbTypes[i] + '</option>';
    }
    $('#dbType').append(option);

//
    $("#openForm").click(function () {
        $('#table2').hide();
        $('#table3').hide();
        $('#dbList').show();
        $('#id01').fadeIn("slow");
    });

    $("#cancelButton").click(function () {
        $('#id01').fadeOut("fast");
    });

    $("#testButton").click(function () {
        var x = $("form").serializeArray();

        var connName = x[0].value;
        var host = x[1].value;
        var dbType = x[2].value;
        var port = x[3].value;
        var instance = x[4].value;
        var userName = x[5].value;
        var password = x[6].value;

        $.post('http://localhost:8080/MavenWebApp/api/v0/databases?test=true',
            {
                connName: connName,
                host: host,
                port: port,
                dbType: dbType,
                instance: instance,
                userName: userName,
                password: password
            }, function (data, status) {
                if (status == "success") {
                    alert(data.Status + "\n" + data.Message);
                }
            })
            .fail(function (data) {
                alert(data.responseText);
            });


    });

    $("#addButton").click(function () {
        var x = $("form").serializeArray();

        var connName = x[0].value;
        var host = x[1].value;
        var dbType = x[2].value;
        var port = x[3].value;
        var instance = x[4].value;
        var userName = x[5].value;
        var password = x[6].value;

        console.log(connName);

        $.post("http://localhost:8080/MavenWebApp/api/v0/databases?test=false",
            {
                connName: connName,
                host: host,
                port: port,
                dbType: dbType,
                instance: instance,
                userName: userName,
                password: password
            }, function (data, status) {
                if (status == "success") {
                    alert("Status: "+data.Status + "\n" + data.Message);
                    $('#id01').fadeOut("fast");
                    location.reload(true);
                }
            })
            .fail(function (data) {
                alert(data.responseText);
            });
    });

    $('#logo1').click(function () {
        location.reload(true);
    });

    $('#home').click(function () {
        location.reload(true);
    });
    $('.closeBtn').click(function () {
        $('#id01').fadeOut("slow");
        location.reload(true);
    });
});
