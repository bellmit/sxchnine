import React, {Component} from 'react';
import { connect } from 'react-redux'
import Aux from '../../hoc/Aux/Aux';
import UserIcon from './UserIcon';
import {Image, Modal, Grid, Label} from "semantic-ui-react";
import stickers from "./stickers.jpg";


class User extends Component {

    state = {
        open: false,
    };

    show = (size) => () => this.setState({size, open: true})
    close = () => this.setState({open: false});

    statusOrder = (status) => {
        if (status === 'WAITING')
            return <Label circular color='orange' />
        else if (status === 'CONFIRMED')
            return <Label circular color='green' />
        else if (status === 'REFUSED')
            return <Label circular color='red' />
        else if (status === 'UNKNOWN')
            return <Label circular color='yellow' />

    };


    render(){
        const {open, size} = this.state;

        let historyBody = <Label color="red">No history with us for now ... Start picking before is too late -> Go Got it !</Label>

        if (this.props.ordersHistory.length > 0){
            historyBody = this.props.ordersHistory.map((order, index) => (
                    <Modal.Content image key={index} scrolling>
                        <Modal.Description>
                            <Grid className="Grid-div">
                                <Grid.Row floated='right'>
                                    <span className="History-Items-Text">Order ID: {order.orderPrimaryKey.orderId}</span>
                                </Grid.Row>
                                <Grid.Row floated='right'>
                                    <span className="History-Items-Text">Order time: {order.orderPrimaryKey.orderTime}</span>
                                </Grid.Row>
                                <Grid.Row>
                                    <Grid className="Grid-div">
                                        <Grid.Row floated='right'>
                                            <Grid.Column floated='right' width={12}>
                                                <span className="History-Items-Text">Status: {order.orderStatus} {this.statusOrder(order.orderStatus)}</span>
                                            </Grid.Column>

                                            <Grid.Column floated='left' width={4}>
                                                <span className="History-Items-Text"><Label tag color='red'>${order.total}</Label></span>
                                            </Grid.Column>
                                        </Grid.Row>
                                    </Grid>
                                </Grid.Row>
                            </Grid>
                            <Grid className="Grid-div">
                                {order.products.map((product, index) => (
                                    <Grid.Row>
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
                          user = {this.props.user}
                          top={this.props.top} topIcon={this.props.topIcon} />

                <div>
                    <Modal size={size} open={open} onClose={this.close}
                           style={{position: 'static', height: 'auto'}}>
                        <Modal.Header><Image src={stickers} fluid style={{height: '220px', width:'100%'}}/></Modal.Header>


                        <span className="History-Resume-Text"> You GOT : </span>
                        {historyBody}
                        <Modal.Actions>
                            {/*<Button color='black'>
                                <span>CHECKOUT</span><Icon name='right chevron' color='yellow'/>
                            </Button>*/}
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