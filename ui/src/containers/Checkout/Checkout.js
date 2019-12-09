import React, {Component} from 'react';
import {Button, Divider, Grid, Input} from "semantic-ui-react";
import Banner from "../../components/Banner/Banner";
import './Checkout.css';
import Contact from "../Contact/Contact";

class Checkout extends Component {

    componentDidMount(): void {
        console.log("did mount");
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        console.log('did update');
    }

    continueAsGuest = () => {
        this.props.history.push('/orders');
    }


    render() {
        return (
            <div>
                <div className="Checkout-Yellow-bar-div"/>
                <div>
                    <header>
                        <Banner {...this.props}/>
                    </header>
                </div>
                <Grid centered className="Checkout-Grid">
                    <Grid.Row floated='left'>
                        <Grid.Column width={6} floated='left'>
                            <p className="Checkout-Message-Text">New to Got it ?!</p>
                        </Grid.Column>

                        <Grid.Column width={6} floated='right'>
                            <p className="Checkout-Message-Text">Got it Member?!</p>
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
                                        <Input inverted placeholder='email address...' className="Checkout-Email-Text" />
                                    </Grid.Column>
                                </Grid.Row>

                                <Grid.Row>
                                    <Grid.Column width={5}>
                                        <p className="Checkout-Email-Text">PASSWORD:</p>
                                    </Grid.Column>

                                    <Grid.Column width={5}>
                                        <Input inverted placeholder='Password' className="Checkout-Email-Text"/>
                                    </Grid.Column>
                                </Grid.Row>
                                <Grid.Row centered>
                                    <button className="Checkout-Continue-Button">
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

export default Checkout;