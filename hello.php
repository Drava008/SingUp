<?php

  $con=mysqli_connect("127.0.0.1","root","111","menber");
  if (mysqli_connect_errno($con))
  {
     echo '{"query_result":"ERROR"}';
  }


  $fullName = $_GET['fullname'];
  $userName = $_GET['username'];
  $passWord = $_GET['password'];
  $phoneNumber = $_GET['phonenumber'];
  $emailAddress = $_GET['emailaddress'];
  $regid = $_GET['regid'];

  $sql = "SELECT id FROM user where phone = '$phoneNumber' or email = '$emailAddress'";
  $result = $con->query($sql);

  if ($result->num_rows > 0) {
      echo '{"query_result":"REWITE"}';
  } else {
    $result = mysqli_query($con,"INSERT INTO user (fullname, username, password, phone, email, regid)
              VALUES ('$fullName', '$userName', '$passWord', '$phoneNumber', '$emailAddress', '$regid')");

    if($result == true) {
        echo '{"query_result":"SUCCESS"}';
    }
    else{
        echo '{"query_result":"FAILURE"}';
    }
  }
  mysqli_close($con);
?>
