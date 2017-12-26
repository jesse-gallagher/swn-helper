import React from "react";
import { Navbar, Nav } from 'react-bootstrap';

import {NavLink, NavGroup} from '@darwino/darwino-react-bootstrap';

export default class Navigator extends React.Component {
    render() {
        return (
            <Navbar inverse={this.props.inverse} collapseOnSelect className="navbar-fixed-side">
                <Navbar.Header>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Nav>
                        <NavLink to="/" exact={true}>Home</NavLink>

                        <NavLink to="/app/players">Players</NavLink>
                        <NavLink to="/app/parties">Parties</NavLink>
                        <NavLink to="/app/characters">Characters</NavLink>
                        <NavLink to="/app/ships">Ships</NavLink>
                        <NavLink to="/app/sectors">Sectors</NavLink>
                        <NavLink to="/app/sectorNodes">Sector Nodes</NavLink>
                        <NavLink to="/app/planets">Planets</NavLink>
                        <NavLink to="/app/factions">Factions</NavLink>
                        <NavLink to="/app/campaigns">Campaigns</NavLink>
                        <NavLink to="/app/loans">Loans</NavLink>

                        <NavGroup title="Developers">
                            <NavLink to="/admin/console">Console</NavLink>
                        </NavGroup>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        );
    }
}