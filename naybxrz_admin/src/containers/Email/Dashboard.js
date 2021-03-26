import React, {Component} from "react";
import {Grid} from "semantic-ui-react";
import Subscription from "./Subscription/Subscription";
import UpdatesUser from "./NewsUser/UpdatesUser";
import "./Dashboard.css";

class Dashboard extends Component {

    render() {

        return (
            <Grid className="main-dashboard-div" centered>
                <Grid.Row stretched>
                    <Grid.Column width={5}>
                        <Subscription {...this.props}/>
                    </Grid.Column>
                    <Grid.Column width={5}>
                        <UpdatesUser {...this.props}/>
                    </Grid.Column>
                </Grid.Row>
            </Grid>
        );
    }
}

export default Dashboard;