/* 
 * (c) Copyright Darwino Inc. 2014-2017.
 */

import React from "react";


export default class Footer extends React.Component {
  render() {
    return (
      <footer id="footer" className={"navbar navbar-default navbar-fixed-bottom footer"+(this.props.inverse ? " navbar-inverse" : "")}>
        <p className="navbar-text">(c) 2017 darwino.com.</p>
      </footer>
    );
  }
}