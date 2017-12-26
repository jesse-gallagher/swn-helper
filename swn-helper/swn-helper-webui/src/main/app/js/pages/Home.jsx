/* 
 * (c) Copyright Darwino Inc. 2014-2017.
 */

import React, { Component } from "react";
import { Jumbotron } from "react-bootstrap";

export default class Home extends Component {
    render() {
        return (
          <Jumbotron>
            <h1>Stars Without Number Helper</h1>
            <p>
              Welcome to your Darwino Application!
            </p>
            <p>
              A helper app to track player and DM needs in Stars Without Number
            </p>
          </Jumbotron>
        );
  }
}