import React from "react";


export default class Footer extends React.Component {
  render() {
    return (
      <footer id="footer" className={"navbar navbar-default navbar-fixed-bottom footer"+(this.props.inverse ? " navbar-inverse" : "")}>
        <p className="navbar-text">&copy; 2017 Jesse Gallagher</p>
      </footer>
    );
  }
}