import React, { Component } from 'react';
import Gallery from '../../components/Gallery/Gallery';
import './Middle.css';
import Carousel from '@brainhubeu/react-carousel';
import {Icon} from "semantic-ui-react";
import {isMobile, isTablet} from 'react-device-detect';

class Middle extends Component {
    state = {
        images: [
            { id: 16176052409540, name: 'Biggie smalls ', url: 'BiggieSmalls.jpeg' },
            { id: 16176099685111, name: 'Paris Week end', url: 'ParisWeekend.jpeg' },
            { id: 16176050051771, name: 'Tupac 2Pac Mike Tyson Wu', url: 'Wu_Tyson.jpeg' },
            { id: 16176054880611, name: 'Paris Tyson Montreal', url: 'ParisStairs.jpeg' }
        ]
    }
    render() {
        let slidePerPage = 3;
        let arrowSize = 'huge'
        if (isMobile){
            slidePerPage = 1;
            arrowSize = 'big';
        } else if (isTablet){
            slidePerPage = 3;
            arrowSize = 'big';
        }
        return (
            <div className="Container-Main-Middle">
                <Carousel style={{textAlign: 'center'}}
                    slidesPerPage={slidePerPage}
                    arrowLeft={<Icon name="arrow left" color='yellow' size={arrowSize}/>}
                    arrowRight={<Icon name="arrow right" color='yellow' size={arrowSize}/>}
                    addArrowClickHandler
                    infinite>
                    {this.state.images.map((image, index) => (
                        <Gallery url={image.url} productId={image.id} key={index} {...this.props} />
                    ))}
                </Carousel>
            </div>
        );
    }

}

export default Middle;