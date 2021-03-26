import React,{Component} from 'react';
import {Grid} from 'semantic-ui-react';
import './Dashboard.css';
import OrdersNumberDashboard from './OrdersNumberDashboard';
import OrdersEvolution from "./OrdersEvolution";

class Dashboard extends Component {
    render() {

        return (
            <div style={{position: "initial" }}>
                <Grid centered size='tiny'>
                    <Grid.Row columns='2'>
                        <Grid.Column width='5'>
                            <OrdersNumberDashboard />
                        </Grid.Column>
                        <Grid.Column width='5'>
                            <OrdersEvolution />
                        </Grid.Column>
                    </Grid.Row>

                </Grid>
            </div>
        )
    }
}

export default Dashboard;