import React, {PureComponent} from 'react';
import {Icon, Grid} from 'semantic-ui-react';
import Aux from '../../hoc/Aux/Aux';
import './Contact.css';


class Contact extends PureComponent {

    openSocial = (social) => {
        window.open(social+'/naybxrz');
    }

    render() {

        return (
            <Aux>
                <div className="Contact-footer">
                    <Grid centered>
                        <Grid.Row columns={3} centered>
                            <Grid.Column>
                                <div>
                                    <a href="/tracking" className="Contact-Link">
                                        <span>Tracking</span>
                                    </a>
                                    <a href="/shipping" className="Contact-Link">
                                        <span>Shipping_Delivery_Returns</span>
                                    </a>
                                    <a href="/customer" className="Contact-Link">
                                        <span>Customer_Service</span>
                                    </a>
                                </div>
                            </Grid.Column>
                            <Grid.Column>
                                <div className="Icon-div">
                                    <Icon name="facebook" size='large' link onClick={() => this.openSocial("https://facebook.com")}/>
                                    <Icon name="instagram" size='large' link onClick={() => this.openSocial("https://instagram.com")}/>
                                    <Icon name="pinterest" size='large' link/>
                                </div>
                            </Grid.Column>

                            <Grid.Column>
                                <div>
                                    <a href="/contactUs" className="Contact-Link">
                                        <span>Contact_US</span>
                                    </a>
                                    <a href="/faq" className="Contact-Link">
                                        <span>FAQ</span>
                                    </a>
                                    <a href="/green" className="Contact-Link">
                                        <span>Green_Brand</span>
                                    </a>
                                </div>
                            </Grid.Column>

                        </Grid.Row>
                        <Grid.Row columns={3} centered>
                            <Grid.Column>
                            </Grid.Column>

                            <Grid.Column width={8}>
                                <div>
                                    <p className="Reserved-right">© 2021 Naybxrz. All rights reserved</p>
                                </div>
                            </Grid.Column>

                            <Grid.Column>
                            </Grid.Column>

                        </Grid.Row>
                    </Grid>
                </div>

            </Aux>
        );
    }
}

export default Contact;