import React, {PureComponent} from 'react';
import Cards from 'react-credit-cards';
import 'react-credit-cards/es/styles-compiled.css';
import {connect} from 'react-redux';
import date from 'date-and-time';
import './Card.css';
import {
    formatCreditCardNumber,
    formatCVC,
    formatExpirationDate,
} from './utils';
import {Dimmer, Label, Loader} from "semantic-ui-react";
import * as actions from "../../store/actions";

class Card extends PureComponent {
    state = {
        number: '',
        name: '',
        expiry: '',
        cvc: '',
        issuer: '',
        focused: '',
        formData: null,
        errorName: ''
    };

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS) {
        console.log("Card updated");
        console.log(this.props.error);
        if (this.props.handledErrors !== undefined &&
             this.props.handledErrors.errorReason !== undefined){
            this.setState({errorName: this.props.handledErrors.errorReason.message});
        }
    }


    handleCallback = ({issuer}, isValid) => {
        if (isValid) {
            this.setState({issuer});
        }
    };

    handleInputFocus = ({target}) => {
        this.setState({
            focused: target.name,
        });
    };

    handleInputChange = ({target}) => {
        if (target.name === 'number') {
            target.value = formatCreditCardNumber(target.value);
        } else if (target.name === 'expiry') {
            target.value = formatExpirationDate(target.value);
        } else if (target.name === 'cvc') {
            target.value = formatCVC(target.value);
        }

        this.setState({[target.name]: target.value});
    };

    handleSubmit = e => {
        e.preventDefault();
        const formData = [...e.target.elements]
            .filter(d => d.name)
            .reduce((acc, d) => {
                acc[d.name] = d.value;
                return acc;
            }, {});

        this.setState({formData});
        this.form.reset();
    };

    handleOrder = () => {
        if (this.state.number !== ''
            && this.state.name !== ''
            && this.state.expiry !== ''
            && this.state.cvc !== '') {

            console.log(this.props);
            console.log('----> products to order');
            console.log(this.createOrder());
            this.props.processOrder(this.createOrder(), this.props.history);
        }

    };

    createOrder() {
        return {
            orderKey: {
                orderId: this.generateId(),
                userEmail: this.props.email,
                orderTime: date.format(new Date(), 'YYYY-MM-DD HH:mm:ss'),
            },
            orderStatus: 'ORDERED',
            products: this.props.productsToOrder,
            paymentInfo: {
                noCreditCard: this.state.number.trim(),
                expDate: this.state.expiry,
                securityCode: this.state.cvc,
                lastName: this.state.name,
                type: 'card'
            },
            userAddress: {
                address: this.props.num + ' ' + this.props.avenue,
                postalCode: this.props.postalCode,
                city: this.props.city,
                country: this.props.country
            },
            paymentStatus: this.props.paymentStatus,
            paymentTime: date.format(new Date(), 'YYYY-MM-DD HH:mm:ss'),
            total: this.props.total
        }
    }

    generateId(){
        return (Date.now().toString(36) + Math.random().toString(36).substr(2, 5)).toUpperCase();
    }

    render() {
        return (
            <div key="Payment">
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <div className="App-payment">
                    <h4 className="Cards-h4">
                        {this.state.errorName !== '' && <Label color='red'>{this.state.errorName}</Label>}</h4>
                    <Cards
                        number={this.state.number}
                        name={this.state.name}
                        expiry={this.state.expiry}
                        cvc={this.state.cvc}
                        focused={this.state.focused}
                        callback={this.handleCallback}
                    />


                    <form className="Cards-form" ref={c => (this.form = c)} onSubmit={this.handleSubmit}>
                        <div className="form-group">
                            <input
                                type="tel"
                                name="number"
                                className="form-control"
                                placeholder="Card Number"
                                pattern="[\d| ]{16,22}"
                                required
                                onChange={this.handleInputChange}
                                onFocus={this.handleInputFocus}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="name"
                                className="form-control"
                                placeholder="Name"
                                required
                                onChange={this.handleInputChange}
                                onFocus={this.handleInputFocus}
                            />
                        </div>
                        <div className="row">
                            <div className="col-6">
                                <input
                                    type="tel"
                                    name="expiry"
                                    className="form-control"
                                    placeholder="MM/YY"
                                    pattern="\d\d/\d\d"
                                    required
                                    onChange={this.handleInputChange}
                                    onFocus={this.handleInputFocus}
                                />
                            </div>
                            <div className="col-6">
                                <input
                                    type="tel"
                                    name="cvc"
                                    className="form-control"
                                    placeholder="CVC"
                                    pattern="\d{3,4}"
                                    required
                                    onChange={this.handleInputChange}
                                    onFocus={this.handleInputFocus}
                                />
                            </div>
                        </div>
                        <input type="hidden" name="issuer"/>
                        <div className="form-actions">
                            <button className="Card-App-btn" onClick={this.handleOrder}>
                                <span className="Card-App-Pay"> ORDER </span>
                            </button>
                        </div>
                        <div className="form-actions">
                            <span className="Card-App-AcceptCondition">By placing your order you agree to our Terms & Conditions, privacy and returns policies . You also consent to some of your data being stored by Naybxrz, which may be used to make future shopping experiences better for you.</span>

                        </div>
                    </form>
                </div>
            </div>

        );
    }
}

const mapStateToProps = state => {
    return {
        productsToOrder: state.productsToOrder.productsToOrder,
        paymentStatus: state.order.paymentStatus,
        handledErrors: state.order.handledErrors,
        loading: state.order.loading,
    }
};

const dispatchToProps = dispatch => {
    return {
        processOrder: (productsToOrder, history) => dispatch(actions.order(productsToOrder, history))
    }
};

export default connect(mapStateToProps, dispatchToProps)(Card);