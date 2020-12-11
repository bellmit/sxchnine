import React, {Component} from 'react';
import {Container, Header, Form, Dimmer, Loader, Grid, Label, Progress, Image} from "semantic-ui-react";
import {connect} from 'react-redux';
import * as actions from "../../store/actions";
import tracking_back from './tracking_back.jpg';
import './Tracking.css';

class Tracking extends Component {

    state = {
        orderId: '',
        email: ''
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    track = () => {
        this.props.trackOrder(this.state.orderId, this.state.email);
    };

    statusOrder = (status) => {
        if (status === 'ORDERED' | status === 'REQUIRED_ACTION' || status === 'WAITING')
            return 20;
        else if (status === 'CONFIRMED')
            return 50;
        else if (status === 'PROCESSING')
            return 79;
        else if (status === 'SHIPPED')
            return 100;

    };

    render() {

        let trackOrders = undefined;
        let trackOrderNotFound = undefined;

        if (this.props.trackOrders.length > 0){
            trackOrders = this.props.trackOrders.map((order, index) => (
                    <Grid key={index}>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="Tracking-Order-Message">
                                    Order ID: {order.orderKey.orderId}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={6}>
                                <span className="Tracking-Order-Message">
                                    Order time: {order.orderKey.orderTime}
                                </span>
                            </Grid.Column>
                            <Grid.Column width={5}>
                                <span>
                                    <Label tag color='red'>${order.total}</Label>
                                </span>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column width={5}>
                                <span className="Tracking-Order-Message">
                                    Order Status:
                                </span>
                            </Grid.Column>
                            <Grid.Column width={10}>
                                {order.orderStatus === 'REFUSED' ?
                                    <Progress size='small' percent={100} error indicating> Refused </Progress> :
                                    <Progress size='small' percent={this.statusOrder(order.orderStatus)} indicating>
                                        <span
                                            className="TabAccount-Progress-Text">ordered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            processing&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            preparing to ship&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            shipped</span></Progress>}
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid container>
                                {order.products.map((product, indexProduct) => (
                                    <Grid.Row key={indexProduct}>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <Image wrapped
                                                   size='small'
                                                   src={product.image}/>
                                        </Grid.Column>
                                        <Grid.Column width={5} style={{left: '40px'}}>
                                            <p className="Tracking-Order-Message">
                                                {product.productName}
                                            </p>
                                            <p className="Tracking-Order-Message">
                                                {product.productColor}
                                            </p>
                                            <p className="Tracking-Order-Message">
                                                {product.productSize}
                                            </p>
                                            <p className="Tracking-Order-Message">
                                                ${product.unitPrice}
                                            </p>
                                        </Grid.Column>
                                    </Grid.Row>
                                ))}
                            </Grid>
                        </Grid.Row>
                    </Grid>
                ))
        }

        if (this.props.trackOrderFound === false) {
            trackOrderNotFound= <Label color="red" size="small">No order found. Are you sure is the right Order ID?</Label>
        }

        let trackBody = trackOrders !== undefined && <div className="Tracking-Order-Form-Div"><Container fluid>{trackOrders}</Container></div>

        return (
            <div>
                <img alt="" src={tracking_back} className="Tracking-Image-div"/>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="Tracking-Form-Div">
                    <Container text className="Tracking-Container-div">
                        <Header>
                            <span className="ContactUs-Message-Text-h1-div">Track your Order:</span>
                            {trackOrderNotFound}
                        </Header>

                        <div className="Tracking-Grid-div">
                            <Grid inverted>
                                <Grid.Row>
                                    <Form.Input required inverted
                                                style={{fontFamily: "American Typewriter, Times", fontSize: '60%'}}
                                                size="mini"
                                                placeholder='Order ID ...'
                                                className="Info-Text"
                                                name='orderId'
                                                value={this.state.orderId}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <Form.Input inverted required size="mini"
                                                style={{fontFamily: "American Typewriter", fontSize: '60%'}}
                                                placeholder='Email ... (optional)'
                                                className="Info-Text"
                                                name='email'
                                                value={this.state.email}
                                                onChange={this.handleChange}/>
                                </Grid.Row>
                                <Grid.Row>
                                    <button className="Tracking-Continue-Button" onClick={this.track}>
                                        <span className="Tracking-Text-Button">TRACK</span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </div>
                    </Container>
                </div>
                {trackBody}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.order.loading,
        trackOrders: state.order.trackOrder,
        trackOrderFound: state.order.trackOrderFound
    }
}

const dispatchToProps = dispatch => {
    return {
        trackOrder: (orderId, email) => dispatch(actions.trackOrder(orderId, email))
    }
}

export default connect(mapStateToProps, dispatchToProps)(Tracking);