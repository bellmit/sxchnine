import React, {Component} from 'react';
import {connect} from "react-redux";
import salute1 from "../Home/salute1.png";
import './Welcome.css';
import Notification from "../Notification/Notification";
import {Grid, Icon} from "semantic-ui-react";
import * as actions from '../../store/actions/index';

class Welcome extends Component {

    signOff = () => { this.props.signOffUser(this.props.history); }


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
                                <Icon name='power off'
                                      color='red'
                                      className="log-off-icon"
                                      onClick={this.signOff} />
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

const dispatchToProps = dispatch => {
    return {
        signOffUser: (history) => dispatch(actions.signOff(history))
    }
}

export default connect(mapStateToProps, dispatchToProps)(Welcome);