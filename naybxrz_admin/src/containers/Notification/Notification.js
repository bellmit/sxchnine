import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Feed, Icon, Label, Popup} from "semantic-ui-react";
import * as actions from './../../store/actions/index';
import Aux from './../../adhoc/Aux/Aux'
import './Notification.css';

class Notification extends Component {

    state = {
        orders: [],
        open: false
    }

    componentDidMount() {
        console.log("call event source");
        this.props.startOrdersNotification(this.props.ordersByMonthCount);
        console.log(this.props.ordersNotificationData.sort((o1, o2)=> new Date(o2.orderKey.orderTime) - new Date(o1.orderKey.orderTime)));
        console.log(this.props.ordersNotificationCount);
    }

    resetNotificationNumber = () => {
        this.setState({open: true})
        this.props.ordersNotificationResetSize();
    }

    redirectToOrder = (orderId) => {
        this.props.getOrderById(orderId, this.props.history);
        this.setState({open: false})
    }

    handleClose = () => {
        this.setState({ open: false })
    }

    render() {
        let ordersPushed = undefined;
        if (this.props.ordersNotificationData !== undefined
            && this.props.ordersNotificationData.length > 0) {
            ordersPushed = <Aux>
                {Object.values(this.props.ordersNotificationData).sort((o1, o2)=> new Date(o2.orderKey.orderTime) - new Date(o1.orderKey.orderTime)).slice(0, 10).map((order, index) => (
                    <Feed.Event key={index}>
                        <Feed.Label>
                            <Icon name="alarm" size="tiny" inverted />
                        </Feed.Label>
                        <Feed.Content>
                            <Feed.Summary>
                                New order <Feed.User><span onClick={() => this.redirectToOrder(order.orderKey.orderId)}>{order.orderKey.orderId}</span></Feed.User> added at <Feed.Date>{order.orderKey.orderTime}</Feed.Date>
                            </Feed.Summary>
                        </Feed.Content>
                    </Feed.Event>

                    ))}
            </Aux>
        }

        let notificationCountLabel = undefined;
        if (this.props.ordersNotificationCount !== 0){
            notificationCountLabel = <Label color="red" floating size="mini">
                {this.props.ordersNotificationCount}
            </Label>
        }

        return (
            <div>
                <Popup pinned
                       position="bottom center"
                       size='small'
                       on='click'
                       open={this.state.open}
                       onOpen={this.resetNotificationNumber}
                       onClose={this.handleClose}
                       trigger={<Icon name="bell outline" size="large">
                           {notificationCountLabel}
                       </Icon>}>

                    <Popup.Content>
                        <Feed className="feed-text">
                        {ordersPushed}
                        </Feed>
                    </Popup.Content>
                </Popup>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        ordersByMonthCount: state.orders.ordersByMonthCount,
        ordersNotificationCount: state.orders.ordersNotificationCount,
        ordersNotificationData: state.orders.ordersNotificationData
    }
}

const dispatchToProps = dispatch => {
    return {
        startOrdersNotification: (firstSize) => dispatch(actions.startOrdersNotification(firstSize)),
        ordersNotificationResetSize: () => dispatch(actions.ordersNotificationResetSize()),
        getOrderById: (orderId, history) => dispatch(actions.orderById(orderId, history))

    }
}

export default connect(mapStateToProps, dispatchToProps)(Notification);