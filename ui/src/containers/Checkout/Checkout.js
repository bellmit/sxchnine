import React, {Component} from 'react';
import {Button, Dimmer, Divider, Grid, Input, Label, Loader} from "semantic-ui-react";
import { connect } from 'react-redux';
import Banner from "../../components/Banner/Banner";
import './Checkout.css';
import Contact from "../Contact/Contact";
import Account from '../Account/Account';
import * as actions from "../../store/actions";


class Checkout extends Component {

    state = {
        email: '',
        password: ''
    }

    componentDidMount(): void {
        console.log("did mount");
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        console.log('did update');
        console.log(this.props.userAuth);
        console.log(prevProps);
        if (prevProps.userAuth.email !== this.props.userAuth.email
            && prevProps.userAuth.password !== this.props.userAuth.password) {
            this.setState({
                email: this.props.userAuth.email,
                password: this.props.userAuth.password
            })
        }
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});


    continueAsGuest = () => {
        this.props.history.push('/orders');
    };

    login = () => {
        this.props.loginUser(this.state.email, this.state.password, this.props.history, true);
    };


    render() {
        return (
            <div>
                <div className="Checkout-Yellow-bar-div"/>
                <div>
                    <header>
                        <Banner {...this.props}/>
                    </header>
                </div>
                <Dimmer active={this.props.loading} page>
                    <Loader content='Loading'/>
                </Dimmer>
                <Grid centered className="Checkout-Grid">
                    <Grid.Row floated='left'>
                        <Grid.Column width={6} floated='left'>
                            <p className="Checkout-Message-Text">New to Got it ?!</p>
                        </Grid.Column>

                        <Grid.Column width={6} floated='right'>
                            <p className="Checkout-Message-Text">Got it Member?!</p>
                            <Account />
                        </Grid.Column>
                    </Grid.Row>

                    <Grid.Row floated='right'>
                        <Grid className="Checkout-Inner-Grid">
                            <Grid.Row centered mobile={2}>
                                <Grid.Column width={6}>
                                    <p className="Checkout-Email-Text">Sign up with:</p>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row centered>
                                <Grid.Column width={3}>
                                    <Button circular color='facebook' icon='facebook' />
                                </Grid.Column>
                                <Grid.Column width={3}>
                                    <Button circular color='twitter' icon='twitter' />
                                </Grid.Column>
                            </Grid.Row>
                            <Divider horizontal>
                                <span className="Checkout-Divider">OR</span>
                            </Divider>
                            <Grid.Row centered>
                                <button className="Checkout-Continue-Button" onClick={this.continueAsGuest}>
                                    <span className="Checkout-Text-Button">Continue as guest</span>
                                </button>
                            </Grid.Row>
                        </Grid>

                        <Grid.Column width={6} floated='right'>
                            <Grid>
                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <p className="Checkout-Email-Text">EMAIL:</p>
                                    </Grid.Column>

                                    <Grid.Column width={5}>
                                        <Input inverted
                                               name='email'
                                               placeholder='Email'
                                               className="Checkout-Email-Text"
                                               value={this.state.email}
                                               onChange={this.handleChange}

                                        />
                                    </Grid.Column>
                                </Grid.Row>

                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <p className="Checkout-Email-Text">PASSWORD:</p>
                                    </Grid.Column>

                                    <Grid.Column width={5}>
                                        <Input inverted
                                               name='password'
                                               placeholder='Password'
                                               type='password'
                                               className="Checkout-Email-Text"
                                               value={this.state.password}
                                               onChange={this.handleChange}
                                        />
                                    </Grid.Column>
                                    {this.props.error !== '' && <Label color="red">{this.props.error}</Label>}
                                </Grid.Row>
                                <Grid.Row centered>
                                    <button className="Checkout-Continue-Button" onClick={this.login}>
                                        <span className="Checkout-Text-Button">SIGN IN</span>
                                    </button>
                                </Grid.Row>
                            </Grid>
                        </Grid.Column>
                    </Grid.Row>
                    <Divider vertical className="Checkout-Divider">
                        <span className="Checkout-Divider">OR</span>
                    </Divider>
                    <Grid.Row centered>
                        <div className="Checkout-Empty-Div"/>
                    </Grid.Row>
                    <Grid.Row>
                        <div className="Checkout-footer">
                            <Contact/>
                        </div>
                    </Grid.Row>
                </Grid>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        userAuth: state.users.userAuth,
        status: state.users.status,
        loading: state.users.loading,
        error: state.users.error
    }
};

const dispatchToProps = dispatch => {
    return {
        loginUser: (email, password, history, order) => dispatch(actions.loginUser(email, password, history, order))
    }
};

export default connect(mapStateToProps, dispatchToProps)(Checkout);