import React from "react";
import { Link } from "react-router-dom";
import { Button } from "react-bootstrap";
import { CursorPage, CursorGrid} from '@darwino/darwino-react-bootstrap'

import Constants from "./Constants";

export class FactionsViewGrid extends CursorGrid {

    // Default values of the properties
    static defaultProps  = {
        databaseId: Constants.DATABASE,
        params: {
            name: "factions"
        },
        ftSearch:true,
        grid: {
            columns:[
                {name: "Name", key: "name"},
                {name: "Creation Date", key: "cdate"}
            ]
        },
        baseRoute: "/app/faction"
    }

    contributeActionBar() {
        return (
            <div key="main">
                <Link to={`${this.props.baseRoute}`} className="btn btn-primary">Create New Faction</Link>
            </div>
        );
    }
}

export default class FactionsVIew extends CursorPage {

    constructor(props,context) {
        super(props,context)
    }
    
    render() {
        return (
            <div>
                <h4>Factions</h4>
                {this.createActionBar()}
                <div>
                    <FactionsViewGrid height={this.state.gridHeight}/>
                </div>
            </div>
        )
    }
}

