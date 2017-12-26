import React from "react";

import { Route, Switch } from 'react-router-dom';

import Home from "./Home";
import Header from "./Header";
import Footer from "./Footer";
import Navigator from "./Navigator";
import {AdminConsole} from '@darwino/darwino-react-bootstrap';

import Player from "./app/Player";
import PlayersView from "./app/Players";
import Party from "./app/Party";
import PartiesView from "./app/Parties";
import Character from "./app/Character";
import CharactersView from "./app/Characters";
import Ship from "./app/Ship";
import ShipsView from "./app/Ships";
import Sector from "./app/Sector";
import SectorsView from "./app/Sectors";
import SectorNode from "./app/SectorNode";
import SectorNodesView from "./app/SectorNodes";
import Planet from "./app/Planet";
import PlanetsView from "./app/Planets";
import Faction from "./app/Faction";
import FactionsView from "./app/Factions";

export default class Layout extends React.Component {
  render() {
    const { location, renderingOptions } = this.props;
    return (
      <div>
        <Header inverse={renderingOptions.headerInverted}/>
        <div className="container-fluid" id="body-container">
          <div className="row">
            <div className="col-sm-3 col-lg-2 sidebar">
              <Navigator location={location} inverse={renderingOptions.leftnavInverted}/>
            </div>
            <div className="col-sm-9 col-lg-10 main" id="content">
              <Switch>
                <Route exact path="/" component={Home}></Route>

                <Route exact path="/app/parties" component={PartiesView}></Route>
                <Route exact path="/app/party/" component={Party}></Route>
                <Route exact path="/app/party/:unid" component={Party}></Route>

                <Route exact path="/app/players" component={PlayersView}></Route>
                <Route exact path="/app/player/" component={Player}></Route>
                <Route exact path="/app/player/:unid" component={Player}></Route>

                <Route exact path="/app/characters" component={CharactersView}></Route>
                <Route exact path="/app/character/" component={Character}></Route>
                <Route exact path="/app/characters/:unid" component={Character}></Route>

                <Route exact path="/app/ships" component={ShipsView}></Route>
                <Route exact path="/app/ship/" component={Ship}></Route>
                <Route exact path="/app/ships/:unid" component={Ship}></Route>

                <Route exact path="/app/sectors" component={SectorsView}></Route>
                <Route exact path="/app/sector/" component={Sector}></Route>
                <Route exact path="/app/sector/:unid" component={Sector}></Route>

                <Route exact path="/app/sectorNodes" component={SectorNodesView}></Route>
                <Route exact path="/app/sectorNode/" component={SectorNode}></Route>
                <Route exact path="/app/sectorNode/:unid" component={SectorNode}></Route>

                <Route exact path="/app/planets" component={PlanetsView}></Route>
                <Route exact path="/app/planet/" component={Planet}></Route>
                <Route exact path="/app/planet/:unid" component={Planet}></Route>

                <Route exact path="/app/factions" component={FactionsView}></Route>
                <Route exact path="/app/faction/" component={Faction}></Route>
                <Route exact path="/app/faction/:unid" component={Faction}></Route>

                <Route exact path="/admin/console" component={AdminConsole}></Route>
              </Switch>
            </div>
          </div>
        </div>
        <Footer inverse={renderingOptions.footerInverted}/>
      </div>
    );
  }
}
