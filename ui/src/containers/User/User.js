import React, {Component} from 'react';
import {connect} from 'react-redux'
import Aux from '../../hoc/Aux/Aux';
import UserIcon from './UserIcon';
import {Grid, Image, Label, Modal, Progress} from "semantic-ui-react";
import stickers from "./stickers.jpg";


class User extends Component {

    state = {
        open: false,
    };

    show = (size) => () => this.setState({size, open: true})
    close = () => this.setState({open: false});

    statusOrder = (status) => {
        if (status === 'ORDERED' || status === 'REQUIRED_ACTION' || status === 'WAITING')
            return 20;
        else if (status === 'PROCESSING')
            return 50;
        else if (status === 'PREPARING')
            return 79;
        else if (status === 'SHIPPED')
            return 100;
    };


    render() {
        const {open, size} = this.state;

        let historyBody = <Label color="red">No history with us for now ... Start picking before is too late -> Go Got
            it !</Label>

        if (this.props.ordersHistory.length > 0) {
            historyBody = this.props.ordersHistory.map((order, index) => (
                <Modal.Content image key={index} scrolling>
                    <Modal.Description>
                        <Grid className="Grid-div">
                            <Grid.Row floated='right'>
                                <Grid.Column width={4}>
                                    <span className="History-Items-Text">Order ID: {order.orderId}</span>
                                </Grid.Column>
                                <Grid.Column width={5}>
                                    <span className="History-Items-Text">Order time: {order.orderTime}</span>
                                </Grid.Column>
                                <Grid.Column floated='left' width={4}>
                                    <span className="History-Items-Text"><Label tag
                                                                                color='red'>${order.total}</Label></span>
                                </Grid.Column>
                            </Grid.Row>
                        </Grid>
                        <Grid className="Grid-div">
                            {order.products.map((product, index) => (
                                <Grid.Row key={index}>
                                    <Grid.Column width={4}>
                                        <Image wrapped size='tiny'
                                               src={product.image}/>
                                    </Grid.Column>
                                    <Grid.Column width={8}>
                                        <span className="History-Items-Text-Header">{product.productName}</span>
                                        <p className="History-Items-Text">{product.productColor}</p>
                                        <p className="History-Items-Text">{product.productSize}</p>
                                        <p className="History-Items-Text">${product.unitPrice}</p>
                                    </Grid.Column>
                                </Grid.Row>
                            ))}
                        </Grid>
                    </Modal.Description>
                </Modal.Content>
            ))
        }

        return (
            <Aux>
                <UserIcon show={this.show('small')}
                          user={this.props.user}
                          top={this.props.top}
                          topIcon={this.props.topIcon}/>

                <div>
                    <Modal size={size} open={open} onClose={this.close}
                           style={{position: 'static', height: 'auto'}}>
                        <Modal.Header>
                            <Image src={stickers} fluid
                                   style={{height: '220px', width: '100%'}}/>
                        </Modal.Header>


                        <span className="History-Resume-Text">You GOT : </span>
                        {historyBody}
                        <Modal.Actions>
                        </Modal.Actions>
                    </Modal>
                </div>
            </Aux>
        );
    }
}

const mapStateToProps = state => {
    return {
        user: state.users.userAuthenticated,
        ordersHistory: state.order.ordersHistory
    }
};

export default connect(mapStateToProps, null)(User);