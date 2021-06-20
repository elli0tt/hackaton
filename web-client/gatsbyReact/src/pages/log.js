import * as React from "react"
import { Link } from "gatsby"
import { Button } from 'react-bootstrap';
import { Form } from 'react-bootstrap';

import Layout from "../components/layout"
import Seo from "../components/seo"


async function loginToken() {
  let fetchOpts = {
    method: 'POST',
    headers: {
    },
  }
  let response = await fetch('http://95.79.175.202:14332/api2/' + 'log/' + 'doc/' + JSON.stringify({
    username: document.getElementById("username").value,
    password: document.getElementById("password").value
  }), fetchOpts).then(function(response) {
    // The response is a Response instance.
    // You parse the data into a useable format using `.json()`
    return response.text();
  }).then(function(data) {
    // `data` is the parsed version of the JSON returned from the above endpoint.
    document.cookie = "token=" + data + "; path=/; expires=Tue, 19 Jan 2038 03:14:07 GMT;"
    window.location.replace("/lk");
    return data;
  });
}

const LogPage = () => (
  <Layout>
    <Seo title="Login" />
    <h1>Авторизация</h1>

    <Form name="log">
      <Form.Group controlId="Username">
        <Form.Control id="username" type="username" placeholder="Имя пользователя" />
      </Form.Group>
      <Form.Group controlId="Password">
        <Form.Control id="password" type="password" placeholder="Пароль" />
      </Form.Group>
      <Button onClick={loginToken}>Войти</Button>
    </Form>

    <p>
      <Link to="/">Вернуться на главную</Link>
    </p>
  </Layout>
)

export default LogPage
