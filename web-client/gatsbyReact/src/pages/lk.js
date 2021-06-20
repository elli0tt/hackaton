import * as React from "react"
import { Link } from "gatsby"
import { Button } from 'react-bootstrap';
import { Form } from 'react-bootstrap';
import { Table } from 'react-bootstrap';

import Layout from "../components/layout"
import Seo from "../components/seo"

function getCookie(name) {
  let matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

async function getData() {
  let fetchOpts = {
    method: 'GET',
    headers: {
    },
  }

  let token = getCookie("token")

  console.log(fetchOpts.body)
  let response = await fetch('http://95.79.175.202:14332/api2/' + token + '/patient/' + document.getElementById("snils").value, fetchOpts)
  .then(function(response) {
    // The response is a Response instance.
    // You parse the data into a useable format using `.json()`
    return response.json();
  }).then(function(data) {
    // `data` is the parsed version of the JSON returned from the above endpoint.
    console.log(data)
    return data;
  });
}

async function getReadings() {
  let fetchOpts = {
    method: 'GET',
    headers: {
    },
  }

  let token = getCookie("token")

  console.log(fetchOpts.body)
  let response = await fetch('http://95.79.175.202:14332/api2/' + token + '/readings/' + document.getElementById("snils").value, fetchOpts)
  .then(function(response) {
    // The response is a Response instance.
    // You parse the data into a useable format using `.json()`
    return response.json();
  }).then(function(data) {
    // `data` is the parsed version of the JSON returned from the above endpoint.
    console.log(data)
    var element = document.createElement('table')
    element.className = "table table-bordered"
    element.innerHTML = constructTable(data)
    return data;
  });
}

async function sendComment() {
  let fetchOpts = {
    method: 'POST',
    headers: {
    },
  }

  let token = getCookie("token")

  console.log(fetchOpts.body)
  let response = await fetch('http://95.79.175.202:14332/api2/' + token + '/comments/' + JSON.stringify({
    "PatientSNILS": document.getElementById("snils").value,
    "comment": document.getElementById("comment").value,
    "dateTime": Date.now()
    }), fetchOpts)
  .then(function(response) {
    // The response is a Response instance.
    // You parse the data into a useable format using `.json()`
    return response.json();
  }).then(function(data) {
    // `data` is the parsed version of the JSON returned from the above endpoint.
    console.log(data)
    alert("Успешно отправлено")
    return data;
  });
}

function constructTable(list) {
  // Getting the all column names
  var cols = Headers(list);
  console.log(cols)

  var element = document.createElement('table');
  var trElem = document.createElement('tr')
  for (var colIndex = 0; colIndex < cols.length; colIndex++)
  {
    if (cols[colIndex] !== "id" && cols[colIndex] !== "patientSNILS") {
      var tdElem = document.createElement('td')
      tdElem.innerText = cols[colIndex]
      trElem.appendChild(tdElem)
    }
  }
  element.appendChild(trElem)
  for (var i = 0; i < list.length; i++) {
    var trElem = document.createElement('tr')
    for (var colIndex = 0; colIndex < cols.length; colIndex++)
    {
      if (cols[colIndex] !== "id" && cols[colIndex] !== "patientSNILS") {
        var tdElem = document.createElement('td')
        if (cols[colIndex] == "dateTime")
        {
          tdElem.innerText = new Date(list[i][cols[colIndex]]).toLocaleString()
        }
        else
        {
          tdElem.innerText = list[i][cols[colIndex]]
        }
        trElem.appendChild(tdElem)
      }
    }
    element.appendChild(trElem)
  }
  document.body.append(element)
}

function Headers(list, selector) {
  var columns = [];
  var header = ('<tr/>');
    
  for (var i = 0; i < list.length; i++) {
      var row = list[i];
        
      for (var k in row) {
          if (columns.indexOf(k) == -1) {
              columns.push(k);
          }
      }
  }
  return columns;
}       

const LkPage = () => (
  <Layout>
    <Seo title="Login" />
    <h1>Личный кабинет</h1>

    <Form name="log">
      <Form.Group controlId="Snils">
        <Form.Control id="snils" placeholder="СНИЛС" />
      </Form.Group>
      <Form.Group controlId="Comment">
        <Form.Control id="comment" placeholder="Введите комментарий для пациента" />
      </Form.Group>
      
      <Button onClick={getReadings}>Получить данные</Button>
      <Button onClick={sendComment}>Отправить комментарий</Button>
    </Form>

    <p>
      <Link to="/">Вернуться на главную</Link>
    </p>
  </Layout>
)

export default LkPage
