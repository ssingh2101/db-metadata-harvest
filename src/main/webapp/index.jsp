<!DOCTYPE html>
<html>
<head>
    <%--<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="script.js"></script>

    <link rel="stylesheet" href="style.css">
</head>

<body>

<div id="logo" style="cursor: pointer">
    <div class="nav-bar"></div>
    <%--<img id="logo1" src="logo.png">--%>
</div>

<ul id="menu">
    <li><a href="#Home" class="button" id="home">Home</a></li>
    <li><a href="#Add" id="openForm">Add New Connection</a></li>
</ul>

<div class="sidenav">
    <table id="sideTable" border="0" style="width: 100%; ">
        <tr id="connList"></tr>
    </table>
</div>

<div class="schemaTable" id="schemaTableArea">
</div>

<div class="dbTables" id="dbTablesArea">
</div>

<div class="table">
    <table id="dbList" border='1'>
        <tr>
            <th>Database Type</th>
            <th>Instance</th>
            <th>Connection Name</th>
            <th>Host</th>
            <th>Port</th>
        </tr>
    </table>
</div>

<div id="id01" class="modal">

    <form class="modal-content animate">
        <div class="connForm">

            <header class="w3-container w3-teal">
                <span class="closeBtn">&chi;</span>
                <h4 id="heading"><b><u>Add new database connection</u></b></h4>
            </header>


            <label for="connName"><b>Connection Name</b></label>
            <input type="text" id="connName" name="connName" placeholder="Connection Name">

            <label for="host"><b>Host</b></label>
            <input type="text" id="host" name="host" placeholder="Host">
            <div class="custom-select">
                <label for="dbType"><b>Database Type</b></label>
                <select id="dbType" name="dbType">
                    <option selected>Please Select</option>
                </select>
            </div>


            <label for="port"><b>Port</b></label>
            <input type="text" id="port" name="port" placeholder="Port">

            <label for="instance"><b>Database Instance</b></label>
            <input type="text" id="instance" name="instance" placeholder="Database Instance">

            <label for="userName"><b>User Name</b></label>
            <input type="text" id="userName" name="userName" placeholder="User Name">

            <label for="password"><b>Password</b></label>
            <input type="password" id="password" name="password" placeholder="Password">


        </div>

        <div class="connForm" style="background-color:#f1f1f1">
            <button type="button" id="cancelButton" class="button button1">Cancel</button>
            <button type="button" id="testButton" class="button button1">Test</button>
            <button type="button" id="addButton" class="button button1">Add</button>
        </div>

    </form>
</div>

</body>
</html>