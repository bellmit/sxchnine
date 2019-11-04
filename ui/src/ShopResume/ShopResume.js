import React, {Component} from "react";
import './ShopResume.css';
import {Button, Grid, Header, Icon, Image, Label, Modal} from "semantic-ui-react";
import Product from "../Product/Product";

class ShopResume extends Component {

    state = {
        products: [
            {
                id: 1, name: 'Classic retro - 90s', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },

        ],
        open: false }

    show = (size) => () => this.setState({ size, open: true })
    close = () => this.setState({ open: false })

    render() {
        const { open, size } = this.state

        return (
            <div>
                <div>
                <Button circular color='black' className="Panier-div" onClick={this.show('small')}>
                    <Icon name="basket shopping" className="Panier-div-icon"/>
                    <Label color='red' floating size='mini'>
                        {this.props.size}
                    </Label>
                </Button>
                </div>

                <div className="Modal-Content-div">
                <Modal size={size} open={open} onClose={this.close} style={{ position: 'static', height:'auto' }} >
                    <Modal.Header><span className="Panier-Resume-Text"> You GOT : </span></Modal.Header>


                    {this.state.products.map((product, index) => (
                    <Modal.Content image>
                        <Image wrapped size='small'
                               src={product.images[0].url}/>
                        <Modal.Description>
                            <Header><span className="Panier-Items-Text-Header">{product.name}</span></Header>
                            <p className="Panier-Items-Text">Black </p>
                            <p className="Panier-Items-Text">Small</p>
                            <p className="Panier-Items-Text">$90</p>
                        </Modal.Description>
                    </Modal.Content>
                    ))}

                    <Modal.Actions>
                        <Button color='black'>
                            <span className="Pay-Text">Pay $$$ </span><Icon name='right chevron' color='yellow'/>
                        </Button>
                    </Modal.Actions>
                </Modal>
                </div>
            </div>
        );
    }

}

export default ShopResume;