import React from 'react';
import {Input, Icon, Grid} from 'semantic-ui-react';
import './Contact.css';
import fb from './fb.svg';
import ig from './ig.svg';
import pr from './pr.svg';

const contact = () => {
    return (
        <div>
{/*        <div className="Container-div">
            <div>
                <a href="#" className="Contact-Link">
                    <span>Contact_US</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Shipping_Delivery_Returns</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Customer_Service</span>
                </a>
            </div>

            <div className="Icon-div">
                <Icon className="Icon" name="facebook" size='large' />
                <Icon className="Icon" name="instagram" size='large'/>
                <Icon className="Icon" name="pinterest" size='large'/>
            </div>

            <div>
                <a href="#" className="Contact-Link">
                    <span>Green_Brand</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Newsletter</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>FAQ</span>
                </a>
            </div>
        </div>*/}
        <div className="Contact-footer">

            <Grid centered>
                <Grid.Row columns={3} centered>
                    <Grid.Column>
                        <div>
                            <a href="#" className="Contact-Link">
                                <span>Contact_US</span>
                            </a>
                            <a href="#" className="Contact-Link">
                                <span>Shipping_Delivery_Returns</span>
                            </a>
                            <a href="#" className="Contact-Link">
                                <span>Customer_Service</span>
                            </a>
                        </div>
                    </Grid.Column>
                    <Grid.Column>
                    <div className="Icon-div">
                        <Icon name="facebook" size='large' />
                        <Icon name="instagram" size='large'/>
                        <Icon name="pinterest" size='large'/>
                    </div>
                    </Grid.Column>

                    <Grid.Column>
                        <div>
                            <a href="#" className="Contact-Link">
                                <span>Green_Brand</span>
                            </a>
                            <a href="#" className="Contact-Link">
                                <span>Newsletter</span>
                            </a>
                            <a href="#" className="Contact-Link">
                                <span>FAQ</span>
                            </a>
                        </div>
                    </Grid.Column>

                </Grid.Row>
                <Grid.Row columns={3} centered>
                    <Grid.Column>
                    </Grid.Column>

                    <Grid.Column width={8}>
                        <div>
                            <p className="Reserved-right">© 2019 got_it. All rights reserved</p>
                        </div>
                    </Grid.Column>

                    <Grid.Column>
                    </Grid.Column>

                </Grid.Row>
            </Grid>
        </div>

        </div>
    );
}

export default contact;