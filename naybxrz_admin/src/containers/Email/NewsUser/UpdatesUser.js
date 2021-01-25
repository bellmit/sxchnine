import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Confirm, Dimmer, Image, Label, Loader, Statistic} from "semantic-ui-react";
import * as actions from './../../../store/actions/index';
import './UpdatesUser.css';

class UpdatesUser extends PureComponent {

    state = {
        openUpdates: false
    }

    componentDidMount() {
        this.props.getUsers();
        this.props.resetSendUpdatesToUsers(false);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.props.getUsers();
    }

    showUpdates = () => this.setState({openUpdates: true})

    handleConfirmUpdates = () => {
        this.props.sendUpdatesToUsers();
        this.setState({openUpdates: false})
    }

    handleCancel = () => this.setState({openUpdates: false})

    render() {

        let updatesSent = undefined
        if (this.props.sendUpdatesToUsersSuccess) {
            updatesSent = <Label color='green'>Grouped Updates sent successfully !</Label>
        }

        return (
            <div>
                <Dimmer active={this.props.sendUpdatesToUsersLoading} page>
                    <Loader content='Sending...'/>
                </Dimmer>

                <Card>
                    <Card.Content>
                        <Card.Header>
                            <Statistic>
                                <Statistic.Value>
                                    <Image src='https://react.semantic-ui.com/images/avatar/large/molly.png' inline
                                           circular/>
                                    {this.props.usersNumber}
                                </Statistic.Value>
                                <Statistic.Label>Users</Statistic.Label>
                            </Statistic>
                        </Card.Header>
                        <Card.Description>
                            Send grouped updates to our <strong>users</strong>
                        </Card.Description>
                        <Card.Content extra>
                            <div className='ui two buttons'>
                                <Button basic color='green' onClick={this.showUpdates}>
                                    Send
                                </Button>
                                <Confirm
                                    open={this.state.openUpdates}
                                    content='Are you sure to send updates to all the users?'
                                    onCancel={this.handleCancel}
                                    onConfirm={this.handleConfirmUpdate}
                                />
                            </div>
                        </Card.Content>
                    </Card.Content>
                    <Card.Content>
                        {updatesSent}
                    </Card.Content>
                </Card>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        usersNumber: state.user.getUsersNumber,
        sendUpdatesToUsersLoading: state.email.sendUpdatesToUsersLoading,
        sendUpdatesToUsersSuccess: state.email.sendUpdatesToUsersSuccess
    }
}

const dispatchToProps = dispatch => {
    return {
        getUsers: () => dispatch(actions.getUsers()),
        sendUpdatesToUsers: () => dispatch(actions.sendUpdatesToUsers()),
        resetSendUpdatesToUsers: (flag) => dispatch(actions.sendUpdatesToUsersSuccess(flag))
    }
}

export default connect(mapStateToProps, dispatchToProps)(UpdatesUser);