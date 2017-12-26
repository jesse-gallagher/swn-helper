import React from "react";
import { Link } from "react-router-dom";
import { Button } from "react-bootstrap";
import { CursorPage, CursorGrid} from '@darwino/darwino-react-bootstrap'

import Constants from "./Constants";


//
// Resusable grid component that can be embedded in other pages
// The component carries its own property values and can contribute to the main page
//
export class CampaignsViewGrid extends CursorGrid {

    // Default values of the properties
    static defaultProps  = {
        databaseId: Constants.DATABASE,
        params: {
            name: "campaigns"
        },
        ftSearch:true,
        grid: {
            columns:[
                {name: "Name", key: "name"},
                {name: "Creation Date", key: "cdate"}
            ]
        },
        baseRoute: "/app/campaign"
    }

    contributeActionBar() {
        return (
            <div key="main">
                <Link to={`${this.props.baseRoute}`} className="btn btn-primary">Create New Campaign</Link>
            </div>
        );
    }
}


//
// Main frame that displays the grid in a page, with an action bar
//
export default class CampaignsView extends CursorPage {

    constructor(props,context) {
        super(props,context)
    }
    
    render() {
        return (
            <div>
                <h4>Characters</h4>
                {this.createActionBar()}
                <div>
                    <CampaignsViewGrid height={this.state.gridHeight}/>
                </div>
            </div>
        )
    }
}

