import React, {Component} from 'react';
import Cards from 'react-credit-cards';
import 'react-credit-cards/es/styles-compiled.css';
import './Card.css';
import {
    formatCreditCardNumber,
    formatCVC,
    formatExpirationDate,
} from './utils';

class Card extends Component {
    state = {
        number: '',
        name: '',
        expiry: '',
        cvc: '',
        issuer: '',
        focused: '',
        formData: null,
    };

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
        const {issuer} = this.state;
        const formData = [...e.target.elements]
            .filter(d => d.name)
            .reduce((acc, d) => {
                acc[d.name] = d.value;
                return acc;
            }, {});

        this.setState({formData});
        this.form.reset();
    };

    render() {
        const {name, number, expiry, cvc, focused, issuer, formData} = this.state;

        return (
            <div key="Payment">
                <div className="App-payment">
                    <h4 className="Cards-h4">PAYMENT:</h4>
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
                                    placeholder="Valid Thru"
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
                            <button className="Card-App-btn"><span className="Card-App-Pay"> ORDER </span></button>
                        </div>
                        <div className="form-actions">
                            <span className="Card-App-AcceptCondition">By placing your order you agree to our Terms & Conditions, privacy and returns policies . You also consent to some of your data being stored by Got_IT, which may be used to make future shopping experiences better for you.</span>

                        </div>
                        <div>
                            <p>number: {number}</p>
                            <p>name: {name}</p>
                            <p>expiry: {expiry}</p>
                            <p>cvc: {cvc}</p>

                        </div>
                    </form>
                </div>
            </div>

        );
    }
}

export default Card;