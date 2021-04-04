import React, {Component} from 'react';
import {Checkbox, Dimmer, Form, Grid, Image, Label, Loader} from "semantic-ui-react";
import {connect} from 'react-redux';
import './Orders.css';
import OrderPlaceBanner from "../../components/Banner/Banner";
import Card from './Card';
import Contact from '../Contact/Contact';
import User from "../User/User";
import * as actions from '../../store/actions/index';

class Orders extends Component {

    state = {
        open: false,
        total: 0,
        firstName: '',
        lastName: '',
        num: '',
        avenue: '',
        city: '',
        postalCode: '',
        country: '',
        email: '',
        loading: false,
        keepInfo: false

    };

    componentDidMount(): void {
        this.setState({total: this.props.productsToOrder.map(p => p.unitPrice).reduce((p1, p2) => p1 + p2, 0)});
        if (this.props.user.email != null) {
            this.props.fetchOrdersHistory(this.props.user.email);
            this.setState({
                firstName: this.props.user.firstName,
                lastName: this.props.user.lastName,
                email: this.props.user.email,
                num: this.props.user.address.number,
                avenue: this.props.user.address.address,
                city: this.props.user.address.city,
                postalCode: this.props.user.address.postalCode,
                country: this.props.user.address.country
            })
        }
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value})

    toggle = () => this.setState((prevState) => ({ keepInfo: !prevState.keepInfo }))

    render() {

        let keepUserInfo = undefined;
        if (this.props.user === ''){
            keepUserInfo = <Grid.Row>
                <Grid.Column mobile={3}
                             computer={1}>
                    <Checkbox toggle
                              checked={this.state.keepInfo}
                              onChange={this.toggle} />
                </Grid.Column>
                <Grid.Column width={4} mobile={8} computer={4}>
                    <span className="User-Keep-Info-Text">
                        Our collections are limited. Our team select only few items.
                        Agree if we can keep your email and we do our effort to let you know when we bring some new cool stuff.
                    </span>
                </Grid.Column>
            </Grid.Row>
        }

        return (
            <div>
                <Dimmer active={this.state.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="Orders-Yellow-bar-div"/>
                <header>
                    <OrderPlaceBanner {...this.props}/>
                </header>
                <div>
                    {this.props.user !== '' && <User {...this.props}
                                                     top="70px"
                                                     topIcon="74px"/>}
                </div>
                <div className="Orders-Bag-Resume">
                    <span className="Orders-Resume-Text">Your Bag : </span>
                </div>
                <div className="Orders-Resume">
                    <Grid columns={2} centered>
                        {this.props.productsToOrder.map(product => (
                            <Grid.Row centered key={product.id + product.productSize}>
                                <Grid.Column width={3}>
                                    <Image src={product.image} size='small' circular/>
                                </Grid.Column>
                                <Grid.Column width={3}>
                                    <span className="Orders-Items-Text-Header">{product.productName}</span>
                                    <p className="Orders-Items-Text">{product.productColor} </p>
                                    <p className="Orders-Items-Text">{product.productSize}</p>
                                    <p className="Orders-Items-Text">${product.unitPrice}</p>
                                </Grid.Column>
                            </Grid.Row>
                        ))}
                        <Grid.Row>
                            <Grid.Column width={3} height={10}>
                                <p className="Orders-Total-Text">TOTAL:</p>
                            </Grid.Column>
                            <Grid.Column width={3}>
                                <Label tag color='red'>${this.state.total}</Label>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div"/>
                        </Grid.Row>
                    </Grid>
                    <Grid centered>
                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">Personal Info:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Form.Input inverted placeholder='first name..'
                                            name='firstName'
                                            value={this.state.firstName}
                                            onChange={this.handleChange}/>
                                <Form.Input inverted placeholder='last name..'
                                            name='lastName'
                                            value={this.state.lastName}
                                            onChange={this.handleChange}/>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">EMAIL ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Form.Input inverted placeholder='email address...'
                                            name='email'
                                            value={this.state.email}
                                            onChange={this.handleChange}/>
                            </Grid.Column>
                        </Grid.Row>

                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">DELIVERY ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Form.Input inverted placeholder='N°'
                                            name='num'
                                            value={this.state.num}
                                            onChange={this.handleChange}/>
                                <Form.Input inverted placeholder='street/avenue'
                                            name='avenue'
                                            value={this.state.avenue}
                                            onChange={this.handleChange}/>
                                <Form.Input inverted placeholder='city'
                                            name='city'
                                            value={this.state.city}
                                            onChange={this.handleChange}/>
                                <Form.Input inverted placeholder='postal code'
                                            name='postalCode'
                                            value={this.state.postalCode}
                                            onChange={this.handleChange}/>
                                <Form.Input inverted placeholder='country'
                                            name='country'
                                            value={this.state.country}
                                            onChange={this.handleChange}/>
                            </Grid.Column>
                        </Grid.Row>
                        {keepUserInfo}
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div"/>
                        </Grid.Row>
                    </Grid>
                    <Card {...this.props}
                          firstName={this.state.firstName}
                          lastName={this.state.lastName}
                          email={this.state.email}
                          num={this.state.num}
                          avenue={this.state.avenue}
                          city={this.state.city}
                          postalCode={this.state.postalCode}
                          country={this.state.country}
                          total={this.state.total}
                          keepInfo={this.state.keepInfo}/>
                    <div className="Orders-footer">
                        <Contact/>
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        productsToOrder: state.productsToOrder.productsToOrder,
        paymentStatus: state.order.paymentStatus,
        user: state.users.userAuthenticated
    }
};

const dispatchToProps = dispatch => {
    return {
        fetchOrdersHistory: (email) => dispatch(actions.fetchOrdersHistory(email))
    }
};

export default connect(mapStateToProps, dispatchToProps)(Orders);