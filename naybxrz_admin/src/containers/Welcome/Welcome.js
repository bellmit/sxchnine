import React, {Component} from 'react';
import {connect} from "react-redux";
import Aux from '../../adhoc/Aux/Aux';
import salute1 from "../Home/salute1.png";
import './Welcome.css';
import Notification from "../Notification/Notification";
import {Grid} from "semantic-ui-react";

class Welcome extends Component {

    render() {
        return (
            <div className="Welcome-Main-Div">
                    <Grid stretched>
                        <Grid.Row>
                            <Grid.Column>
                                <img src={salute1} alt="salute" className="img-props"/>
                            </Grid.Column>
                            <Grid.Column width={3}>
                                <span className="Welcome-Message-Div">
                                    Hi ! {this.props.authenticatedUser.firstName}
                                </span>
                            </Grid.Column>
                            <Grid.Column>
                                <Notification {...this.props}/>
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
            </div>
        )
    }
}

const mapStateToProps = state => {
    return {
        authenticatedUser: state.user.authenticatedUser
    }
}

export default connect(mapStateToProps)(Welcome);