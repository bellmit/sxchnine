import React, {PureComponent} from 'react';
import {connect} from 'react-redux';
import {Button, Card, Confirm, Dimmer, Image, Label, Loader, Statistic} from "semantic-ui-react";
import * as actions from './../../../store/actions/index';
import './Subscription.css';

class Subscription extends PureComponent {

    state = {
        openSubscription: false
    }

    componentDidMount() {
        this.props.subscribedUsers();
        this.props.getUsers();
        this.props.resetSubscriptionSent(false);
        this.props.resetSendUpdatesToUsers(false);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.props.subscribedUsers();
        this.props.getUsers();
    }

    showSubscription = () => this.setState({openSubscription: true})

    handleConfirmSubscription = () => {
        this.props.sendEmailToSubscribers();
        this.setState({openSubscription: false})
    }

    handleCancel = () => this.setState({openSubscription: false})

    render() {

        let subscriptionSent = undefined
        if (this.props.subscriptionUsersSuccess) {
            subscriptionSent = <Label color='green'>Grouped Email sent successfully !</Label>
        }

        return (
            <div>
                <Dimmer active={this.props.subscriptionLoading} page>
                    <Loader content='Sending...'/>
                </Dimmer>

                <Card>
                    <Card.Content>
                        <Card.Header>
                            <Statistic>
                                <Statistic.Value>
                                    <Image src='https://react.semantic-ui.com/images/avatar/small/joe.jpg' inline
                                           circular/>
                                    {this.props.subscribedUsersNumber}
                                </Statistic.Value>
                                <Statistic.Label>Subscribers</Statistic.Label>
                            </Statistic>
                        </Card.Header>
                        <Card.Description>
                            Send grouped update to our <strong>subscribers</strong>
                        </Card.Description>
                        <Card.Content extra>
                            <div className='ui two buttons'>
                                <Button basic color='green' onClick={this.showSubscription}>
                                    Send
                                </Button>
                                <Confirm
                                    open={this.state.openSubscription}
                                    content='Are you sure to send update to all the subscribers?'
                                    onCancel={this.handleCancel}
                                    onConfirm={this.handleConfirmSubscription}
                                />
                            </div>
                        </Card.Content>
                    </Card.Content>
                    <Card.Content>
                        {subscriptionSent}
                    </Card.Content>
                </Card>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        subscribedUsersNumber: state.user.subscribedUsersNumber,
        subscriptionLoading: state.email.subscriptionLoading,
        subscriptionUsersSuccess: state.email.subscriptionUsersSuccess,

        usersNumber: state.user.getUsersNumber,
        sendUpdatesToUsersLoading: state.email.sendUpdatesToUsersLoading,
        sendUpdatesToUsersSuccess: state.email.sendUpdatesToUsersSuccess
    }
}

const dispatchToProps = dispatch => {
    return {
        subscribedUsers: () => dispatch(actions.subscribedUsers()),
        sendEmailToSubscribers: () => dispatch(actions.subscriptionUsers()),
        resetSubscriptionSent: (flag) => dispatch(actions.subscriptionUsersSuccess(flag)),

        getUsers: () => dispatch(actions.getUsers()),
        sendUpdatesToUsers: () => dispatch(actions.sendUpdatesToUsers()),
        resetSendUpdatesToUsers: (flag) => dispatch(actions.sendUpdatesToUsersSuccess(flag))
    }
}

export default connect(mapStateToProps, dispatchToProps)(Subscription);